package dev.kobalt.appfilter.extension

import dev.kobalt.appfilter.entity.RemoteEntity
import dev.kobalt.core.components.job.JobManager
import dev.kobalt.core.components.network.httpClient
import dev.kobalt.core.extension.JsonObject
import dev.kobalt.core.extension.toMap
import io.ktor.client.request.forms.FormDataContent
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.http.Parameters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import org.jsoup.nodes.Element

fun JobManager.load(filterParameters: Map<String, String>, callback: (RemoteEntity) -> Unit) {
    launch {
        parse(readDataFromCache()).takeIf { it.filters.isNotEmpty() }?.let {
            withContext(Dispatchers.Main) { callback.invoke(it) }
        } ?: run {
            fetch(filterParameters, callback)
        }
    }
}

fun JobManager.fetch(filterParameters: Map<String, String>, callback: (RemoteEntity) -> Unit) {
    launch {
        runCatching {
            httpClient().use {
                when {
                    filterParameters.isEmpty() -> it.get<String>("https://appfilter.net")
                    else -> it.post("https://appfilter.net") {
                        body = FormDataContent(Parameters.build {
                            filterParameters.forEach { entry ->
                                append(entry.key, entry.value)
                            }
                        })
                    }
                }
            }.let {
                saveDataToCache(it, JsonObject(filterParameters).toString())
                parse(readDataFromCache())
            }
        }.getOrDefault(
            RemoteEntity(
                emptyList(),
                emptyList()
            )
        ).let {
            withContext(Dispatchers.Main) { callback.invoke(it) }
        }
    }
}

private fun JobManager.saveDataToCache(data: String, values: String) {
    application.native.cacheDir.resolve("data").also { it.createNewFile() }.writeText(data)
    application.native.cacheDir.resolve("values").also { it.createNewFile() }.writeText(values)
}

private fun JobManager.readDataFromCache(): Pair<String, String> {
    return Pair(
        application.native.cacheDir.resolve("data").also { it.createNewFile() }.readText(),
        application.native.cacheDir.resolve("values").also { it.createNewFile() }.readText()
    )
}

private fun parse(html: Pair<String, String>): RemoteEntity {
    val document = Jsoup.parse(html.first)
    val form = document.select("body").select("form").find { it.attr("action") == "/" }
    val body = document.select("table#mainTable").select("tbody")

    val values =
        html.second.takeIf { it.isNotEmpty() }?.let { JsonObject(html.second).toMap() }
            ?: emptyMap()

    val filters = form?.select("td")?.mapNotNull { element ->
        element.children().firstOrNull()
    }?.mapNotNull { element ->
        val name = element.attr("name")
        val value = (values.getOrElse(name) { "" } as? String).orEmpty()
        when (element.tagName()) {
            "input" -> when (element.attr("type")) {
                "text" -> RemoteEntity.Filter.Input(
                    name = name,
                    text = (element.parentNode() as? Element)?.ownText()?.replace(":", "")
                        .orEmpty(),
                    value = value
                )
                else -> null
            }
            "select" -> RemoteEntity.Filter.Select(
                name = name,
                text = (element.parentNode() as? Element)?.ownText()?.replace(":", "").orEmpty(),
                value = value,
                values = element.children().associate {
                    it.attr("value") to it.text()
                }
            )
            else -> null
        }
    }.orEmpty()

    val packages = body?.select("tr")?.mapNotNull { it.select("td") }?.map { elements ->
        RemoteEntity.Entry(
            name = elements[0].allElements[2].text(),
            link = elements[0].allElements[2].attr("href"),
            image = elements[0].allElements[1].attr("src"),
            ratingTotal = elements[1].text(),
            ratingAverage = elements[2].text(),
            iap = elements[3].text(),
            ads = elements[4].text(),
            price = elements[5].text(),
            iapRange = elements[6].text(),
            category = elements[7].text().replace("_", " "),
            contentRating = elements[8].text(),
            lastUpdate = elements[9].text(),
            developer = elements[10].text(),
            description = elements[0].allElements[2].attr("title")
        )
    }.orEmpty()

    return RemoteEntity(filters, packages)
}
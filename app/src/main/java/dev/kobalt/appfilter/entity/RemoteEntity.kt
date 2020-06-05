package dev.kobalt.appfilter.entity

data class RemoteEntity(
    val filters: List<Filter>,
    val entries: List<Entry>
) {

    sealed class Filter(
        val name: String,
        val text: String,
        val value: String
    ) {
        class Input(
            name: String,
            text: String,
            value: String
        ) : Filter(name, text, value)

        class Select(
            name: String,
            text: String,
            value: String,
            val values: Map<String, String>
        ) : Filter(name, text, value)
    }

    data class Entry(
        val name: String,
        val link: String,
        val image: String,
        val ratingTotal: String,
        val ratingAverage: String,
        val iap: String,
        val ads: String,
        val price: String,
        val iapRange: String,
        val category: String,
        val contentRating: String,
        val lastUpdate: String,
        val developer: String,
        val description: String
    )

}
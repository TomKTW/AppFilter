package dev.kobalt.core.components.job

import dev.kobalt.core.application.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlin.coroutines.CoroutineContext

class JobManager(val application: Application) : CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = GlobalScope.coroutineContext

}

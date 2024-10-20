package io.github.teambutterpl.butterlib.api.resourcepack.models

interface BaseModels {
    val models: List<BaseModel>

    fun list(): List<BaseModel> {
        return models
    }
}
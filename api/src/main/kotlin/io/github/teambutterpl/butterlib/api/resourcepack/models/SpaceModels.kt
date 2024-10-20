package io.github.teambutterpl.butterlib.api.resourcepack.models

class SpaceModels: BaseModels {
    override val models: List<BaseModel>

    init {
        val spaceNoSplit = ImageFile("/resource_pack_default/space/space_nosplit.png", "/assets/space/textures/font/space_nosplit.png")
        val spaceSplit = ImageFile("/resource_pack_default/space/space_split.png", "/assets/space/textures/font/space_split.png")

        val lang = JsonFile("/resource_pack_default/space/en_us.json", "/assets/space/lang/en_us.json")

        val font = JsonFile("/resource_pack_default/space/default.json", "/assets/space/font/default.json")

        models = listOf(
            spaceNoSplit,
            spaceSplit,
            lang,
            font
        )
    }
}
package io.github.teambutterpl.butterlib.api.resourcepack.models

class FontJsonModels(fontFileName: String): BaseModels {
    override val models: List<BaseModel>

    init {
        val staticModels = mutableListOf<StaticModel>()
        for (i in 1..6) {
            val path = "/assets/minecraft/font/font_8_$i.json"
            val json = """
                {
                    "providers": [
                        {
                            "type": "ttf",
                            "file":"minecraft:${fontFileName.replace("/", "")}",
                 			"shift": [0, ${4 + (i-1) * 10}],
                 			"size": 8.0,
                 			"oversample": 16.0
                        }
                    ]
                }
            """.trimIndent()
            val model = object : StaticModel {
                override val outputPath = path
                override val data = json.toByteArray()
            }
            staticModels.add(model)
        }

        models = staticModels.toList()
    }
}
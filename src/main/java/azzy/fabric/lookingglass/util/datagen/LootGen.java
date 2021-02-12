package azzy.fabric.lookingglass.util.datagen;

import com.google.common.base.Charsets;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.apache.commons.io.FileUtils;

import java.io.IOException;

public class LootGen {

    public static void genSimpleBlockDropTable(Metadata metadata, Block block) {

        Identifier id = Registry.ITEM.getId(block.asItem());

        String json = "{\n" +
                "  \"type\": \"minecraft:block\",\n" +
                "  \"pools\": [\n" +
                "    {\n" +
                "      \"rolls\": 1,\n" +
                "      \"entries\": [\n" +
                "        {\n" +
                "          \"type\": \"minecraft:item\",\n" +
                "          \"name\": \"" + id.toString() + "\"\n" +
                "        }\n" +
                "      ],\n" +
                "      \"conditions\": [\n" +
                "        {\n" +
                "          \"condition\": \"minecraft:survives_explosion\"\n" +
                "        }\n" +
                "      ]\n" +
                "    }\n" +
                "  ]\n" +
                "}";

        try {
            FileUtils.writeStringToFile(metadata.genDataJson(id.getPath(), "", Metadata.DataType.BLOCK_LOOT), json, Charsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

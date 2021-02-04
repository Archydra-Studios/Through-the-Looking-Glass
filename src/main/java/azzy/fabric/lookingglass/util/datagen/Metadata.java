package azzy.fabric.lookingglass.util.datagen;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import static azzy.fabric.lookingglass.LookingGlassCommon.FFLog;

public class Metadata {
    public final String resourcePath;
    public final String dataPath;

    public Metadata(String id) {
        this.resourcePath = Paths.get(System.getProperty("user.dir")).getParent().toString() + File.separator + "src" + File.separator + "main" + File.separator + "resources" + File.separator + "assets" + File.separator + id;
        this.dataPath = Paths.get(System.getProperty("user.dir")).getParent().toString() + File.separator + "src" + File.separator + "main" + File.separator + "resources" + File.separator + "data" + File.separator + id;
    }

    public File genResourceJson(String name, String path, ResourceType type) {
        if(!path.isEmpty()) {
            path = path + File.separator;
        }
        File file = new File(resourcePath + File.separator + type.path + File.separator + path + name + ".json");
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    public File genResourceJson(String name, ResourceType type) {
        return genResourceJson(name, "", type);
    }

    enum ResourceType {
        BLOCKSTATE("blockstates"),
        ITEM_MODEL("models" + File.separator + "item"),
        BLOCK_MODEL("models" + File.separator + "block");

        ResourceType(String path) {
            this.path = path;
        }

        public final String path;
    }
}

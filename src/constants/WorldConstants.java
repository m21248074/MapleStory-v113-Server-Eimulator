package constants;

import java.util.Map;
import java.util.HashMap;

public class WorldConstants {

    public static interface Option {

        public int getWorld();

        public String name();
    }

    public static enum WorldOption implements Option {

        泰勒熊(16),
        神獸(15),
        皮卡啾(14),
        鯨魚號(13),
        電擊象(12),
        海努斯(11),
        巴洛古(10),
        蝴蝶精(9),
        火獨眼獸(8),
        木妖(7),
        三眼章魚(6),
        綠水靈(5),
        藍寶(4),
        緞帶肥肥(3),
        星光精靈(2),
        菇菇寶貝(1),
        雪吉拉(0);

        private final int world;

        private static final Map<Integer, WorldOption> lookup = new HashMap<>();

        static {
            for (WorldOption w : WorldOption.values()) {
                lookup.put(w.getWorld(), w);
            }
        }

        private WorldOption(int world) {
            this.world = world;
        }

        @Override
        public int getWorld() {
            return world;
        }

        public static WorldOption getById(int id) {
            return lookup.getOrDefault(id, 雪吉拉);
        }
    }   

}
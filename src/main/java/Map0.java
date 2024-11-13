import net.catech_software.engine.physics.map.GameMap;
import net.catech_software.engine.render.model.LoadScene;
import net.catech_software.engine.render.model.Model;
import net.catech_software.engine.render.model.TextureCache;
import org.joml.Vector3f;

public class Map0 extends GameMap {
  public Map0(TextureCache cache) {
    super(new Model(LoadScene.loadScene("assets/models/map0/map0.gltf"), cache));
    this.minBoundingBox = new Vector3f(-100f, 0f, -100f);
    this.maxBoundingBox = new Vector3f(100f, 0f, 100f);
  }

  @Override
  public void free() {
    if (this.model != null) this.model.free();
    super.free();
  }
}

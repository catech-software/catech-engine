import net.catech_software.engine.physics.object.GameObject;
import net.catech_software.engine.render.model.LoadScene;
import net.catech_software.engine.render.model.Model;
import net.catech_software.engine.render.model.TextureCache;
import org.joml.Vector3f;

public class WaterBottle extends GameObject {
  public WaterBottle(Model model) {
    super(model);
    this.minBoundingBox = new Vector3f(-0.054f, -0.13f, -0.054f);
    this.maxBoundingBox = new Vector3f(0.054f, 0.13f, 0.054f);
  }

  public WaterBottle(TextureCache cache) {
    this(new Model(LoadScene.loadScene("assets/models/WaterBottle/WaterBottle.gltf"), cache));
  }

  @Override
  public void free() {
    if (this.model != null) this.model.free();
    super.free();
  }
}

import net.catech_software.engine.physics.object.GameObject;
import net.catech_software.engine.render.model.LoadScene;
import net.catech_software.engine.render.model.Model;
import net.catech_software.engine.render.model.TextureCache;

public class WaterBottle extends GameObject {
  public WaterBottle(Model model) {
    super(model);
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

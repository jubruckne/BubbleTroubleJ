package com.jubruckne.bubbletrouble;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;

public class Button extends Entity {
    protected String caption;

    public Button(Map map, String caption, float x, float y, float w, float h) {
        super(map, x, y, w, h);
        this.caption = caption;
    }

    private void create_texture() {
        BitmapFont font = new BitmapFont();

        OrthographicCamera camera = new OrthographicCamera();
        camera.setToOrtho(true, (int)(width)*2, (int)(height)*2);
        camera.update();
        SpriteBatch batch = new SpriteBatch();
        batch.setProjectionMatrix(camera.combined);

        ShapeRenderer shapeRenderer = new ShapeRenderer();
        shapeRenderer.setAutoShapeType(true);
        shapeRenderer.setProjectionMatrix(camera.combined);

        FrameBuffer fb = new FrameBuffer(Pixmap.Format.RGBA8888, (int)(width)*2, (int)(height)*2, true);
        TextureRegion tr = new TextureRegion(fb.getColorBufferTexture());

        fb.begin();

        ScreenUtils.clear(0f, 0f, 0f, 0);
        shapeRenderer.setColor(0.8f, 0.8f, 0.8f, 1f);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.rect(1, 1, width * 2 - 2, height * 2 - 2);
        shapeRenderer.rect(2, 2, width * 2 - 4, height * 2 - 4);
        shapeRenderer.end();

        shapeRenderer.setColor(0.4f, 0.4f, 0.4f, 0.5f);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.rect(3, 3, width * 2 - 6, height * 2 - 6);
        shapeRenderer.end();


        batch.begin();
        batch.setColor(1f, 1f, 1f, 1f);
        font.getData().setScale(4);
        font.draw(batch, caption, 20 * 2, (int) Math.floor(height * 1.5));
        font.getData().setScale(1);
        batch.end();

        Pixmap pm = Pixmap.createFromFrameBuffer(0, 0, fb.getWidth(), fb.getHeight());
        PixmapIO.writePNG(new FileHandle("screenshot.png"), pm);

        fb.end();

        batch.dispose();

        this.texture = tr;
    }

    public void draw(SpriteBatch batch, ShapeRenderer shapeRenderer) {
        batch.enableBlending();

        if(texture == null) {
            create_texture();
        }

        batch.begin();
        if(highlight) { batch.setColor(Color.YELLOW); }
        batch.draw(texture, body.getPosition().x - width / 2, body.getPosition().y - height / 2, width, height);
        batch.setColor(Color.WHITE);
        batch.end();
    }
}

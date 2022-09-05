package com.jubruckne.bubbletrouble;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class SpriteSheet {
    private Texture image;
    private Game game;

    public SpriteSheet(final Game game) {
        image = new Texture("blob.png");

        //decode the PNG into a pixel array, aka Pixmap
        Pixmap pixels = new Pixmap(Gdx.files.internal("blob.png"));
        Pixmap p2 = new Pixmap(pixels.getWidth(), pixels.getHeight(), Pixmap.Format.RGBA8888);

        Gdx.app.log("", pixels.getFormat().toString());
        Gdx.app.log("", p2.getFormat().toString());

        for (int x = 0; x < pixels.getWidth(); x++) {
            for (int y = 0; y < pixels.getHeight(); y++) {
                Color color = new Color(pixels.getPixel(x, y));

                if (color.r < 0.20f && color.g < 0.20f && color.b < 0.20f) {
                    color.a = 0f;
                }

                p2.drawPixel(x, y, Color.rgba8888(color));
            }
        }

        image = new Texture(p2);
        pixels.dispose();
        p2.dispose();

        this.game = game;
    }

    public TextureRegion Tower() {
        return new TextureRegion(image, 185, 15, 150, 150);
    }

    /*
        public Sprite Bub(int x, int y) {
            return get_sprite(x, y, 1, 1);
        }

        public Sprite Bob(int x, int y) {
            return get_sprite(x, y, 1, 3);
        }

        public Sprite Robot(int x, int y) {
            return get_sprite(x, y, 1, 5);
        }

        public Sprite Wall1(int x, int y) {
            return get_sprite(x, y, 20, 32);
        }

        private Sprite get_sprite(int posX, int posY, int x, int y) {
            Sprite s = new Sprite(image,
                    1 + (x + 1) * 17,
                    1 + (y + 1) * 17,
                    16,
                    16);
            s.setPosition(posX, posY);
            s.setScale(3);
            s.setAlpha(1f);

            return s;
        }
    */
    public void dispose() {
        image.dispose();
    }
}

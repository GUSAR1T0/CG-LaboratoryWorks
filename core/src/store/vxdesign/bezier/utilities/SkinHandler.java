package store.vxdesign.bezier.utilities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public final class SkinHandler {
    public static final BitmapFont FONT;
    public static final Label.LabelStyle LABEL_STYLE;
    public static final Slider.SliderStyle SLIDER_STYLE;
    public static final SelectBox.SelectBoxStyle SELECT_BOX_STYLE;

    private static final Pixmap SLIDER_BACKGROUND;
    private static final Pixmap SLIDER_KNOB;
    private static final Pixmap SCROLL_PANE_BACKGROUND;
    private static final Pixmap SCROLL_PANE_SCROLL;
    private static final Pixmap SCROLL_PANE_SCROLL_KNOB;
    private static final Pixmap LIST_SELECTION;
    private static final Pixmap SELECT_BOX_BACKGROUND;

    static {
        FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/font.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        fontParameter.size = 28;
        fontParameter.color = new Color(50 / 255f, 50 / 255f, 50 / 255f, 1f);
        FONT = fontGenerator.generateFont(fontParameter);

        LABEL_STYLE = new Label.LabelStyle(FONT, new Color(5 / 255f, 5 / 255f, 5 / 255f, 1f));

        SLIDER_BACKGROUND = new Pixmap(1, 10, Pixmap.Format.RGB565);
        SLIDER_BACKGROUND.setColor(new Color(50 / 255f, 50 / 255f, 50 / 255f, 0.5f));
        SLIDER_BACKGROUND.fill();
        TextureRegionDrawable sliderBackgroundDrawable = new TextureRegionDrawable(new TextureRegion(new Texture(SLIDER_BACKGROUND)));

        SLIDER_KNOB = new Pixmap(10, 20, Pixmap.Format.RGB565);
        SLIDER_KNOB.setColor(new Color(1 / 255f, 1 / 255f, 1 / 255f, 1f));
        SLIDER_KNOB.fill();
        TextureRegionDrawable sliderKnobDrawable = new TextureRegionDrawable(new TextureRegion(new Texture(SLIDER_KNOB)));

        SLIDER_STYLE = new Slider.SliderStyle(sliderBackgroundDrawable, sliderKnobDrawable);

        SCROLL_PANE_BACKGROUND = new Pixmap(1, 1, Pixmap.Format.RGB565);
        SCROLL_PANE_BACKGROUND.setColor(new Color(250 / 255f, 250 / 255f, 250 / 255f, 0.5f));
        SCROLL_PANE_BACKGROUND.fill();
        TextureRegionDrawable scrollPaneBackgroundDrawing = new TextureRegionDrawable(new TextureRegion(new Texture(SCROLL_PANE_BACKGROUND)));

        SCROLL_PANE_SCROLL = new Pixmap(10, 10, Pixmap.Format.RGB565);
        SCROLL_PANE_SCROLL.setColor(new Color(200 / 255f, 200 / 255f, 200 / 255f, 0.5f));
        SCROLL_PANE_SCROLL.fill();
        TextureRegionDrawable scrollPaneScrollDrawing = new TextureRegionDrawable(new TextureRegion(new Texture(SCROLL_PANE_SCROLL)));

        SCROLL_PANE_SCROLL_KNOB = new Pixmap(10, 10, Pixmap.Format.RGB565);
        SCROLL_PANE_SCROLL_KNOB.setColor(new Color(1 / 255f, 1 / 255f, 1 / 255f, 0.5f));
        SCROLL_PANE_SCROLL_KNOB.fill();
        TextureRegionDrawable scrollPaneScrollKnobDrawing = new TextureRegionDrawable(new TextureRegion(new Texture(SCROLL_PANE_SCROLL_KNOB)));

        LIST_SELECTION = new Pixmap(1, 1, Pixmap.Format.RGB565);
        LIST_SELECTION.setColor(new Color(150 / 255f, 150 / 255f, 150 / 255f, 0.5f));
        LIST_SELECTION.fill();
        TextureRegionDrawable listSelectionDrawing = new TextureRegionDrawable(new TextureRegion(new Texture(LIST_SELECTION)));

        SELECT_BOX_BACKGROUND = new Pixmap(1, 1, Pixmap.Format.RGB565);
        SELECT_BOX_BACKGROUND.setColor(new Color(250 / 255f, 250 / 255f, 250 / 255f, 0.5f));
        SELECT_BOX_BACKGROUND.fill();
        TextureRegionDrawable selectBoxBackgroundDrawing = new TextureRegionDrawable(new TextureRegion(new Texture(SELECT_BOX_BACKGROUND)));

        ScrollPane.ScrollPaneStyle scrollPaneStyle = new ScrollPane.ScrollPaneStyle(scrollPaneBackgroundDrawing,
                scrollPaneScrollDrawing, scrollPaneScrollKnobDrawing, scrollPaneScrollDrawing, scrollPaneScrollKnobDrawing);
        List.ListStyle listStyle = new List.ListStyle(FONT, Color.WHITE, Color.WHITE, listSelectionDrawing);
        SELECT_BOX_STYLE = new SelectBox.SelectBoxStyle(FONT, Color.WHITE, selectBoxBackgroundDrawing, scrollPaneStyle, listStyle);
    }

    public static void dispose() {
        SLIDER_BACKGROUND.dispose();
        SLIDER_KNOB.dispose();
        SCROLL_PANE_BACKGROUND.dispose();
        SCROLL_PANE_SCROLL.dispose();
        SCROLL_PANE_SCROLL_KNOB.dispose();
        LIST_SELECTION.dispose();
        SELECT_BOX_BACKGROUND.dispose();
    }
}

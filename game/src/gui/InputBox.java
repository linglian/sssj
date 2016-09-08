package gui;

//********************************************
import static gui.ChoiceBox.menuImage;
import static gui.TextField.uiImage;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

//*类名:InputBox
//*作者:凌恋      时间:2016-8-18 11:48:17
//* 介绍:
//* 
//* 
//* 
//* 
//********************************************
public class InputBox extends Dialog {

    TextField textField;
    String str;
    public static Image[] uiImage;
    public static boolean isFirst = true;
    public static Image[][] menuImage;
    boolean isInput = false;
    Button[] button;
    GuiImage[][] guiImage2;

    public InputBox() {
        super();
    }

    public InputBox(int x, int y, int width, int height, int size, String text) {
        this();
        textField = new TextField((int) (width * 0.15f), (int) (height * 0.75f), (int) (width * 0.65f), (int) (height * 0.2f), size);
        this.setText(text);
        this.x = x;
        this.y = y;
        this.width = width;
        this.textStyle = GameGUI.TEXT_CENTER;
        this.height = height;
        this.isViewable = true;
        this.textColor = Color.RED;
        button = new Button[1];
        guiImage2 = new GuiImage[2][];
        guiImage2[0] = new GuiImage[1];
        guiImage2[1] = new GuiImage[1];
    }

    public InputBox(int x, int y, int width, int height, int size) {
        this();
        textField = new TextField((int) (width * 0.15f), (int) (height * 0.75f), (int) (width * 0.65f), (int) (height * 0.2f), size);
        this.setText(text);
        this.x = x;
        this.y = y;
        this.width = width;
        this.textStyle = GameGUI.TEXT_CENTER;
        this.height = height;
        this.isViewable = true;
        this.textColor = Color.RED;
        button = new Button[1];
        guiImage2 = new GuiImage[2][];
        guiImage2[0] = new GuiImage[1];
        guiImage2[1] = new GuiImage[1];
    }

    public void init() {
        isInput = false;
        textField.init();
        if (isFirst) {
            isFirst = false;
            uiImage = new Image[1];
            menuImage = new Image[2][];
            menuImage[0] = new Image[1];
            menuImage[1] = new Image[1];
            try {
                uiImage[0] = ImageIO.read(new File("image\\UI\\ChoiceBox.png"));
                menuImage[0][0] = ImageIO.read(new File("image\\UI\\guanbi_0.png"));//关闭
                menuImage[1][0] = ImageIO.read(new File("image\\UI\\guanbi_1.png"));//关闭
            } catch (IOException ex) {
                isFirst = true;
            }
        }
        reset(x, y, width, height);
    }

    @Override
    public void click(MouseEvent e) {
        int size = gameGUI.size();
        textField.isActive = true;
        int x = e.getX();
        int y = e.getY();
        for (int i = 0; i < size; i++) {
            GameGUI temp = gameGUI.get(i);
            int id = temp.getId();
            if (temp.isRange(e.getX() - this.x, e.getY() - this.y)) {
                switch (id) {
                    case 0:
                        this.str = null;
                        isInput = true;
                        this.setViewable(false);
                        break;
                }
                if (temp.equals(textField)) {
                    if (x > this.x + (int) (width * 0.15f) + (int) (width * 0.65f * 0.85f) && y >=this.y+ (int) (height * 0.75f) && y <= this.y+(int) (height * 0.95f)) {
                        textField.str = textField.text;
                        textField.isPut = true;
                        textField.text = new String("");
                        textField.size = 0;
                    }
                }
                break;
            }
        }
    }

    public void typed(KeyEvent e) {
        textField.typed(e);
    }

    @Override
    public void move(MouseEvent e) {
        int size = gameGUI.size();
        for (int i = 0; i < size; i++) {
            GameGUI temp = gameGUI.get(i);
            if (temp.equals(textField) == false) {
                if (temp.isRange(e.getX() - x, e.getY() - y)) {
                    temp.isActive = true;
                    temp.move(e);
                } else {
                    temp.exit(e);
                }
            }
        }
    }

    public void reset(int x, int y, int width, int height) {
        isInput = false;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.guiImage.clear();
        int h = (int) (this.height * 0.25f);
        int w = (int) (this.width * 0.44f);
        int m = 10;
        button[0] = new Button(this.width - this.width / m, 0, this.width / m, this.width / m);
        button[0].setId(0);
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 1; j++) {
                guiImage2[i][j] = new GuiImage(0, 0, width / m, height / m, menuImage[i][j]);
            }
        }
        textField.reset((int) (width * 0.15f), (int) (height * 0.75f), (int) (width * 0.65f), (int) (height * 0.2f));
        this.addImage(new GuiImage(0, 0, width, height, uiImage[0]));
        this.addGUI(textField);
        this.addGUI(button[0]);
    }

    @Override
    public void draw(Graphics g) {
        if (this.isViewable == false) {
            return;
        }
        if (textField.isPut) {
            this.isInput = true;
            this.str = textField.str;
            this.setViewable(false);
        }
        int size = gameGUI.size();
        for (int i = 0; i < size; i++) {
            GameGUI temp = gameGUI.get(i);
            int id = temp.getId();
            if (id != -1) {
                if (temp.isActive) {
                    temp.clearGuiImage();
                    temp.addImage(guiImage2[1][id]);
                } else {
                    temp.clearGuiImage();
                    temp.addImage(guiImage2[0][id]);
                }
            }
        }
        super.draw(g);
    }

    public boolean isInput() {
        return isInput;
    }

    public void setIsInput(boolean isInput) {
        this.isInput = isInput;
    }

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }

    public TextField getTextField() {
        return textField;
    }

    public void setTextField(TextField textField) {
        this.textField = textField;
    }

}

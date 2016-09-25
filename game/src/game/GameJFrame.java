package game;
                
//********************************************
import npc.Build;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLJPanel;
import com.jogamp.opengl.util.FPSAnimator;
import gui.GameGUI;
import java.awt.Font;
import java.awt.Toolkit;
import javax.swing.JFrame;
import listener.GameKeyListener;
import listener.GameMouseListener;
import listener.GameMouseMotionListener;
import listener.GameMouseWheelListener;
import listener.GameWindowListener;
import npc.NpcObject;

//*类名:GameJFrame
//*作者:凌恋      时间:2016-8-13 16:34:38
//* 介绍:
//* 
//* 
//* 
//* 
//********************************************
public class GameJFrame extends JFrame {

    GameManage gameManage;
    private final GLProfile profile;
    private final GLCapabilities capabilities;
    private final GameGLEventListener gameGLEventListener;
    private final NpcGLEventListener npcGLEventListener;

    public GameJFrame(GameManage gameManage) {
        Toolkit kit = Toolkit.getDefaultToolkit();
        gameManage.setFrame(this);
        this.gameManage = gameManage;
        profile = GLProfile.get(GLProfile.GL2);
        capabilities = new GLCapabilities(profile);

        GameGLJPanel panel = new GameGLJPanel(capabilities, gameManage);
        GLJPanel npcPanel = new GLJPanel(capabilities);
        gameGLEventListener = new GameGLEventListener(gameManage);
        npcGLEventListener = new NpcGLEventListener(gameManage);
        gameManage.setGameGLEventListener(gameGLEventListener);
        gameManage.setNpcPanel(npcPanel);
        gameManage.setPanel(panel);
        panel.addGLEventListener(gameGLEventListener);
        npcPanel.addGLEventListener(npcGLEventListener);

        GameEvent gameEvent = new GameEvent(gameManage);
        gameManage.setEvent(gameEvent);
        panel.setBounds(0, 0, kit.getScreenSize().width, kit.getScreenSize().height);
        this.setContentPane(panel);
        this.setUndecorated(true);
        this.setResizable(false);
        this.setLayout(null);

        panel.addKeyListener(new GameKeyListener(gameManage));
        panel.addMouseListener(new GameMouseListener(gameManage));
        panel.addMouseMotionListener(new GameMouseMotionListener(gameManage));
        panel.addMouseWheelListener(new GameMouseWheelListener(gameManage));

        this.addWindowListener(new GameWindowListener(gameManage));
        this.setBounds(0, 0, kit.getScreenSize().width, kit.getScreenSize().height);
        npcPanel.setBounds(WIDTH, WIDTH, WIDTH, WIDTH);
    }

    public void init() {
        GameBuild gameBuild = new GameBuild(gameManage);
        gameManage.setBuild(gameBuild);
        GameNpc gameNpc = new GameNpc(gameManage);
        gameManage.setNpc(gameNpc);
        GameMap gameMap = new GameMap(250, 250, gameManage);
        gameManage.setMap(gameMap);
        GameUser gameUser = new GameUser(125, 125, gameManage);
        gameManage.setUser(gameUser);
        GameThread thread = new GameThread(gameManage);
        gameManage.setThread(thread);
        gameManage.getNpcPanel().setBounds((int) (this.getWidth() * 0.345f), (int) (this.getHeight() * 0.77f), (int) (this.getWidth() * 0.15f), (int) (this.getHeight() * 0.19f));
        gameManage.getPanel().init();
        this.setVisible(true);
        FPSAnimator animator = new FPSAnimator(gameManage.getPanel(), gameManage.getUser().getGameSpeed(), true);
        gameManage.setAnimator(animator);
        animator.start();
        thread.start();
        GameGLJPanel.gameFont = GameGLJPanel.gameFont.deriveFont(Font.PLAIN, (int) (gameManage.getFrame().getHeight() * 0.02f));
        GameGUI.setFont(GameGLJPanel.gameFont);
        this.add(gameManage.getNpcPanel());

    }
}

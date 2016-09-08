package toolkit.music;

//********************************************
import java.applet.Applet;
import java.applet.AudioClip;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

//*类名:Music
//*作者:凌恋      时间:2016-8-13 16:56:19
//* 介绍:
//* 
//* 
//* 
//* 
//********************************************
public class Music {

    AudioClip audio;

    public Music(File file) {
        try {
            audio = Applet.newAudioClip(file.toURI().toURL());
        } catch (MalformedURLException ex) {
            System.out.println();
        }
    }

    public Music(URL url) {
        audio = Applet.newAudioClip(url);
    }

    public void play() {
        audio.play();
    }

    public void stop() {
        audio.stop();
    }

    public void loop() {
        audio.loop();
    }
}

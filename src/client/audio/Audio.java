package client.audio;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;

import com.jme.math.Vector3f;
import com.jme.scene.Spatial;
import com.jmex.audio.AudioSystem;
import com.jmex.audio.AudioTrack;
import com.jmex.audio.AudioTrack.TrackType;
import common.Pair;

public class Audio {
	
	private final AudioSystem asys;
	private final Vector3f tmpv = new Vector3f();
	
	private HashMap<SoundEffect, AudioTrack> tracks = new HashMap<SoundEffect, AudioTrack>();
	private LinkedList<Entry> queue = new LinkedList<Entry>();
	
	public Audio() {
		asys = AudioSystem.getSystem();
		
		loadTracks();
	}
	
	private void loadTracks() {
		try {
			AudioTrack pew = getSFX(new File("../files/pew.wav").toURI().toURL(), false);
	        pew.setEnabled(true);
	        tracks.put(SoundEffect.Pew, pew);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	private AudioTrack getSFX(URL resource, boolean loop) {
		AudioTrack sound = asys.createAudioTrack(resource, false);
		sound.setType(TrackType.POSITIONAL);
		sound.setRelative(false);
		sound.setLooping(loop);
		return sound;
	}
	
	public void queueSound(SoundEffect s, Spatial source) {
		queue.addLast(new Entry(s,source));
	}
	
	public void update(Vector3f earpos) {
		while(!queue.isEmpty()) {
			final Entry e = queue.removeFirst();
			final AudioTrack trk = tracks.get(e.item1);
			float dist = earpos.distance(e.item2.getWorldTranslation());
			float vol;
			if (dist < 400f) {
				vol = 1 - 0.0025f*dist;
				vol = vol > 1f ? 1f : vol;
			} else {
				vol = 0;
			}
			trk.setVolume(vol);
			trk.play();
		}
	}
	
	private class Entry extends Pair<SoundEffect,Spatial> {
		public Entry(SoundEffect e, Spatial f) {
			super(e, f);
		}
	}
}

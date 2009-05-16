package client.audio;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import com.jme.math.Vector3f;
import com.jmex.audio.AudioSystem;
import com.jmex.audio.AudioTrack;
import com.jmex.audio.AudioTrack.TrackType;

import common.Pair;
import common.world.MobileObject;
import common.world.ObjectType;
import common.world.ObjectTypeSets;
import common.world.WorldObject;

public class Audio {
	
	private final AudioSystem asys;
	private final Vector3f tmpv1 = new Vector3f();
	private final Vector3f tmpv2 = new Vector3f();
	private final Vector3f tmpv3 = new Vector3f();
	private final Vector3f tmpv4 = new Vector3f();
	
	private HashMap<SoundEffect, AudioTrack> tracks = new HashMap<SoundEffect, AudioTrack>();
	
	private LinkedList<Entry> queue = new LinkedList<Entry>();
	private LinkedList<Emit> emits = new LinkedList<Emit>();
	private HashMap<SoundEffect, URL> seFiles;
	
	public Audio() {
		asys = AudioSystem.getSystem();
		
		initFileTable();
		loadTracks();
	}
	
	private void initFileTable() {
		seFiles = new HashMap<SoundEffect, URL>();
		try {
			seFiles.put(SoundEffect.Pew, new File("../files/pew.wav").toURI().toURL());
			seFiles.put(SoundEffect.Weee, new File("../files/weee.wav").toURI().toURL());
			seFiles.put(SoundEffect.Splode, new File("../files/prrh.wav").toURI().toURL());
			seFiles.put(SoundEffect.Shh, new File("../files/shh.wav").toURI().toURL());
			seFiles.put(SoundEffect.Weow, new File("../files/weow.wav").toURI().toURL());
		} catch (MalformedURLException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	private void loadTracks() {
		for (SoundEffect se : seFiles.keySet()) {
			AudioTrack at = loadAudioTrack(se);
			tracks.put(se, at);
		}
	}
	
	private AudioTrack loadAudioTrack(SoundEffect se) {
		AudioTrack at = asys.createAudioTrack(seFiles.get(se), false);
		at.setType(TrackType.POSITIONAL);
		at.setRelative(false);
        at.setEnabled(true);
        at.setLooping(false);
        return at;
	}
	
	public void queueSound(SoundEffect s, WorldObject source) {
		queue.addLast(new Entry(s,source));
	}
	
	public void update(Vector3f earpos, Vector3f earmovement) {
		asys.update();
		
		//one-shots
		while(!queue.isEmpty()) {
			final Entry e = queue.removeFirst();
			final AudioTrack trk = tracks.get(e.item1);
			setVolume(trk, earpos, e.item2.getWorldTranslation());
			setPitch(trk,earpos,earmovement, e.item2);
			trk.play();
		}
		
		//emitted, looping sounds
		final Iterator<Emit> it = emits.iterator();
		while (it.hasNext()) {
			Emit e = it.next();
			if (e.item2.getParent() == null) {
				e.item1.stop();
				it.remove();
			} else {
				setVolume(e.item1, earpos, e.item2.getWorldTranslation());
				setPitch(e.item1, earpos, earmovement, e.item2);
			}
		}
	}
	
	private void setPitch(AudioTrack trk, Vector3f earpos, Vector3f earmov, WorldObject src) {
		if (ObjectTypeSets.mobileObjects.contains(src.getType())) {
			setPitch(trk, earpos, earmov, src.getWorldTranslation(), 
					((MobileObject)src).getMovement());
		}
	}
	
	private void setPitch(AudioTrack trk, Vector3f earpos, Vector3f earmov, Vector3f srcpos, Vector3f srcmov) {
		Vector3f olddist = earpos.subtract(srcpos, tmpv1);
		Vector3f newdist = earpos.add(earmov, tmpv2).subtract(srcpos.add(srcmov, tmpv3), tmpv4);
		float distdiff = olddist.length() - newdist.length(); 
		
		float pitch = 1f + distdiff*0.001f;
		trk.setPitch(pitch);
	}

	private void setVolume(AudioTrack trk, Vector3f earpos, Vector3f srcpos) {
		float dist = earpos.distance(srcpos);
		float vol;
		if (dist < 200f) {
			vol = 1f - 0.005f*dist;
			vol = vol > 1f ? 1f : vol;
		} else {
			vol = 0;
		}
		trk.setVolume(vol);
	}
	
	public void addEmit(SoundEffect se, WorldObject source) {
		AudioTrack at = loadAudioTrack(se);
		at.setVolume(0f);
		at.setLooping(true);
		at.play();
		emits.addLast(new Emit(at, source));
	}
	
	private class Entry extends Pair<SoundEffect,WorldObject> {
		public Entry(SoundEffect se, WorldObject sp) {
			super(se, sp);
		}
	}
	
	private class Emit extends Pair<AudioTrack,WorldObject> {
		public Emit(AudioTrack at, WorldObject sp) {
			super(at, sp);
		}
	}
}

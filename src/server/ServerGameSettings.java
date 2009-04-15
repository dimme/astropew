package server;

import java.io.IOException;

import com.jme.system.AbstractGameSettings;

public class ServerGameSettings extends AbstractGameSettings {
	public void clear() throws IOException {
	}

	public String get(String name, String defaultValue) {
		return defaultValue;
	}

	public int getAlphaBits() {
		return 0;
	}

	public boolean getBoolean(String name, boolean defaultValue) {
		return defaultValue;
	}

	public byte[] getByteArray(String name, byte[] bytes) {
		return null;
	}

	public int getDepth() {
		return 0;
	}

	public int getDepthBits() {
		return 0;
	}

	public double getDouble(String name, double defaultValue) {
		return defaultValue;
	}

	public float getFloat(String name, float defaultValue) {
		return defaultValue;
	}

	public int getFramerate() {
		return 0;
	}

	public int getFrequency() {
		return 0;
	}

	public int getHeight() {
		return 0;
	}

	public int getInt(String name, int defaultValue) {
		return defaultValue;
	}

	public long getLong(String name, long defaultValue) {
		return defaultValue;
	}

	public Object getObject(String name, Object obj) {
		return obj;
	}

	public String getRenderer() {
		return "";
	}

	public int getSamples() {
		return 0;
	}

	public int getStencilBits() {
		return 0;
	}

	public int getWidth() {
		return 0;
	}

	public boolean isFullscreen() {
		return false;
	}

	public boolean isMusic() {
		return false;
	}

	public boolean isSFX() {
		return false;
	}

	public boolean isVerticalSync() {
		return false;
	}

	public void save() throws IOException {
	}

	public void set(String name, String value) {
	}

	public void setAlphaBits(int alphaBits) {
	}

	public void setBoolean(String name, boolean value) {
	}

	public void setByteArray(String name, byte[] bytes) {
	}

	public void setDepth(int depth) {
	}

	public void setDepthBits(int depthBits) {
	}

	public void setDouble(String name, double value) {
	}

	public void setFloat(String name, float value) {
	}

	public void setFramerate(int framerate) {
	}

	public void setFrequency(int frequency) {
	}

	public void setFullscreen(boolean fullscreen) {
	}

	public void setHeight(int height) {
	}

	public void setInt(String name, int value) {
	}

	public void setLong(String name, long value) {
	}

	public void setMusic(boolean musicEnabled) {
	}

	public void setObject(String name, Object obj) {
	}

	public void setRenderer(String renderer) {
	}

	public void setSFX(boolean sfxEnabled) {
	}

	public void setSamples(int samples) {
	}

	public void setStencilBits(int stencilBits) {
	}

	public void setVerticalSync(boolean vsync) {
	}

	public void setWidth(int width) {
	}
}

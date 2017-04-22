package de.fh_bielefeld.geograph.API.src;

public class OSMApi {

	public static final String	api_uri			= "http://api.openstreetmap.org/";
	public static final String	api_test_uri	= "http://api06.dev.openstreetmap.org/";

	public static enum CALLS {
		Capabilities("/api/capabilities"), BoundingBoxMap("/api/0.6/map"), // GET /api/0.6/map?bbox=left,bottom,right,top
		Permissions("/api/0.6/permissions");

		private final String	call_uri;

		private CALLS(final String call_uri) {
			this.call_uri = call_uri;
		}

		@Override
		public String toString() {
			return call_uri;
		}
	}

	// TODO: HTTP Basic Authentication

	public OSMApi() {

	}
}

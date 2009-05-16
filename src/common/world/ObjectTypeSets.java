package common.world;

import java.util.Collection;
import java.util.HashSet;

public class ObjectTypeSets {
	
	public static final Collection<ObjectType> mobileObjects = 
		new ObjectTypeSet(new ObjectType[] {
			ObjectType.Missile,
			ObjectType.Ship
		}
	);
	
	private static class ObjectTypeSet extends HashSet<ObjectType> {
		private static final long serialVersionUID = 1L;

		public ObjectTypeSet(ObjectType[] objecttypes) {
			super();
			for (ObjectType ot : objecttypes) {
				add(ot);
			}
		}
	}
}

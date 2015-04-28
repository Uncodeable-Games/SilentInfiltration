package Exceptions;

@SuppressWarnings("serial")
public class ComponentNotFoundEx extends Exception {
	public <T> ComponentNotFoundEx(int entity, Class<T> componentType) {
		super("Das Entity: "+entity+" besitzt nicht die Componente: "+componentType.getSimpleName()+" !");
	}
}

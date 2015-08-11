package de.ivu.fare.e4.annotations.context;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.swt.widgets.Widget;

/** Stores markers by type which they are able to handle */
public class MarkerRegistry {


    /* TypeOfWidgetA:
     *  TypeOfMarker1 : markerInstance1
     *  TypeOfMarker2 : markerInstance2
     *
     *
     *  e.g.
     *
     *  WidgetCombo:
     *      StatusVisualizingMarker : instance
     *      RequiredFieldMarker : instance
     */
    Map<Class<? super Widget>, Map<Class<? super IMarker>, IMarker>> registry = new HashMap<Class<? super Widget>, Map<Class<? super IMarker>, IMarker>>();



    public static <T extends Widget> void register(IEclipseContext context, Class<? super T> typeOfWidget, IMarker marker, Class<? extends IMarker>... interfaceTypesForMarkerToRegister) {
        if (context.get(MarkerRegistry.class) == null) {
            context.set(MarkerRegistry.class, new MarkerRegistry());
        }
        context.get(MarkerRegistry.class).register(typeOfWidget, marker, interfaceTypesForMarkerToRegister);
    }

    public <T extends Widget> void register(Class<? super T> typeOfWidget, IMarker marker, Class<? extends IMarker>... interfaceTypesForMarkerToRegister) {

        Map<Class<? super IMarker>, IMarker> mapTypeOfMarkerToInstanceMarker = registry.get(typeOfWidget);
        if(mapTypeOfMarkerToInstanceMarker == null){
            mapTypeOfMarkerToInstanceMarker = new HashMap<Class<? super IMarker>, IMarker>();

            Class typeOfWidgetErased = typeOfWidget;
            registry.put(typeOfWidgetErased, mapTypeOfMarkerToInstanceMarker);
        }


        if (mapTypeOfMarkerToInstanceMarker.get(marker.getClass()) != null) {
            throw new IllegalStateException("A registered IMarker already exists for type " + typeOfWidget);
        }

        Class typeOfMarkerErased = marker.getClass();
        mapTypeOfMarkerToInstanceMarker.put(typeOfMarkerErased, marker);

        for(Class typeOfMarkerToRegister : interfaceTypesForMarkerToRegister){
            mapTypeOfMarkerToInstanceMarker.put(typeOfMarkerToRegister, marker);
        }
    }


    public static <W extends Widget, M extends IMarker> M getMarkerUncheckedWidgetType(IEclipseContext context, Class typeOfWidget, Class<M> typeOfMarker) {
        if(!Widget.class.isAssignableFrom(typeOfWidget)){
            throw new IllegalArgumentException("Can not retrieve markers, if the typeOfWidget does not inherit form Widget");
        }
        Class<? extends Widget> typeOfWidgetCasted = typeOfWidget;

        return getMarker(context, typeOfWidgetCasted, typeOfMarker);
    }

    /**
     * Retrieves the marker, associates with a special widget.
     *
     * @param context
     * @param typeOfWidget - the type of widget, which the marker was registered to. Normally this is a composite like "WidgetValidatedLabeled"
     * @param typeOfMarker - the type of marker to retrieve.
     * @return
     */
    public static <W extends Widget, M extends IMarker> M getMarker(IEclipseContext context, Class<W> typeOfWidget, Class<M> typeOfMarker) {
        if (context.get(MarkerRegistry.class) == null) {
            return null;
        }

        // check marker for current type
        M markerCurrentType = context.get(MarkerRegistry.class).getMarker(typeOfWidget, typeOfMarker);
        if (markerCurrentType != null) {
            return markerCurrentType;
        }

        // check marker for parent types, if parent types inherit from widget
        if (typeOfWidget.getSuperclass() != null) {

            // the supertype must be not null AND inherit from Widget
            Class superType = typeOfWidget.getSuperclass();

            // can assign from Widget?
            if(Widget.class.isAssignableFrom(superType)){
                Class<? extends Widget> superTypeWidget = superType;

                M result = getMarker(context, superTypeWidget, typeOfMarker);
                return result;
            }
        }

        return null;
    }


    public <W extends Widget, M extends IMarker> M getMarker(Class<? super W> typeOfWidget, Class<? super M> typeOfMarker) {
        M resultMarkerInstance = null;

        Map<Class<? super IMarker>, ? super IMarker> markersOfWidget = registry.get(typeOfWidget);
        if(markersOfWidget != null){
            resultMarkerInstance = (M) markersOfWidget.get(typeOfMarker);
        }
        return resultMarkerInstance;
    }
}

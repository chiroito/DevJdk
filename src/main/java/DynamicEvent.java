import jdk.jfr.*;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DynamicEvent {

    public static void main(String[] args) throws IOException {
        EventFactory f1 = dynamicEvent1();
        EventFactory f2 = dynamicEvent2();

        Recording r = new Recording();
        r.enable(f1.getEventType().getName());
        r.enable(f2.getEventType().getName());
        r.start();

        Event event1 = f1.newEvent();
        event1.set(0, "message");
        event1.commit();

        Event event2 = f2.newEvent();
        event2.set(0, "hello, world!");
        event2.set(1, 4711);
        event2.commit();

        r.dump(Path.of("dynamic_dump.jfr"));
        r.close();
    }

    private static EventFactory dynamicEvent1() {
        List<AnnotationElement> typeAnnotations = new ArrayList<>();
        typeAnnotations.add(new AnnotationElement(Name.class, "com.example.HelloWorld"));
        typeAnnotations.add(new AnnotationElement(Label.class, "Hello World"));
        typeAnnotations.add(new AnnotationElement(Description.class, "Helps programmer getting started"));

        List<AnnotationElement> fieldAnnotations = new ArrayList<>();
        fieldAnnotations.add(new AnnotationElement(Label.class, "Message"));

        List<ValueDescriptor> fields = new ArrayList<>();
        fields.add(new ValueDescriptor(String.class, "message", fieldAnnotations));

        return EventFactory.create(typeAnnotations, fields);
    }

    private static EventFactory dynamicEvent2(){
        List<ValueDescriptor> fields = new ArrayList<>();

        List<AnnotationElement> messageAnnotations = Collections.singletonList(new AnnotationElement(Label.class, "Message"));
        fields.add(new ValueDescriptor(String.class, "message", messageAnnotations));

        List<AnnotationElement> numberAnnotations = Collections.singletonList(new AnnotationElement(Label.class, "Number"));
        fields.add(new ValueDescriptor(int.class, "number", numberAnnotations));

        String[] category = { "Example", "Getting Started" };
        List<AnnotationElement> eventAnnotations = new ArrayList<>();
        eventAnnotations.add(new AnnotationElement(Name.class, "com.example.HelloWorld"));
        eventAnnotations.add(new AnnotationElement(Label.class, "Hello World"));
        eventAnnotations.add(new AnnotationElement(Description.class, "Helps programmer getting started"));
        eventAnnotations.add(new AnnotationElement(Category.class, category));

        return EventFactory.create(eventAnnotations, fields);
    }
}
package yo.zaenk.jaxb.annot.strip;

import com.sun.codemodel.JAnnotatable;
import com.sun.codemodel.JAnnotationUse;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JFieldVar;
import com.sun.tools.xjc.Options;
import com.sun.tools.xjc.Plugin;
import com.sun.tools.xjc.outline.ClassOutline;
import com.sun.tools.xjc.outline.Outline;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;

import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Simpla plugin which removes {@code @Xml*} annotations
 *
 * Created by zaenk on 2/25/2017.
 */
public class StripAnnotationPlugin extends Plugin {

    private static final Logger log = Logger.getLogger(StripAnnotationPlugin.class.getName());

    @Override
    public String getOptionName() {
        return "Xannot-strip-yo";
    }

    @Override
    public String getUsage() {
        return null;
    }

    @Override
    public boolean run(final Outline outline, Options options, ErrorHandler errorHandler) throws SAXException {
        log("################################################################################################");
        for (final ClassOutline classOutline : outline.getClasses()) {
            removeAnnotations(classOutline.implClass);
            classOutline.implClass.fields().values().forEach(this::removeAnnotations);
        }
        log("################################################################################################");
        return true;
    }

    private void removeAnnotations(JAnnotatable element) {
        Map<String, JAnnotationUse> annotations = element.annotations().stream()
            .collect(Collectors.toMap(
                    annotation -> annotation.getAnnotationClass().name(),
                    annotation -> annotation
            ));
        annotations.forEach((className, annotation) -> {
            if (className.matches("^Xml.*$")) {
                element.removeAnnotation(annotation);
                log(className + " removed");
            } else {
                log(className);
            }
        });
    }

    private void log(String str) {
        System.out.println(str);
    }
}

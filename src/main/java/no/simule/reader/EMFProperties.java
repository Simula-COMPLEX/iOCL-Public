package no.simule.reader;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.xmi.impl.EcoreResourceFactoryImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.emf.ecore.xmi.impl.XMLResourceFactoryImpl;
import org.eclipse.emf.mapping.ecore2xml.Ecore2XMLPackage;
import org.eclipse.emf.mapping.ecore2xml.util.Ecore2XMLResource;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.resource.UML212UMLResource;
import org.eclipse.uml2.uml.resource.UML22UMLResource;
import org.eclipse.uml2.uml.resource.UMLResource;

import java.util.Map;

/**
 * This class util class constrains init methods to initialize UML RecourseSet
 *
 * @author Muhammad Hammad
 * @version 1.0
 * @since 2016-04-15
 */
public class EMFProperties {

    public static void registerResourceFactories() {
        Map<String, Object> extensionFactoryMap = Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap();
        extensionFactoryMap.put(UMLResource.FILE_EXTENSION, UMLResource.Factory.INSTANCE);

        extensionFactoryMap.put(Ecore2XMLResource.FILE_EXTENSION, Ecore2XMLResource.Factory.INSTANCE);
        extensionFactoryMap.put("ecore", new EcoreResourceFactoryImpl());

        extensionFactoryMap.put(UML22UMLResource.FILE_EXTENSION, UML22UMLResource.Factory.INSTANCE);
        extensionFactoryMap.put(UMLResource.FILE_EXTENSION, UML22UMLResource.Factory.INSTANCE);
        extensionFactoryMap.put(UMLResource.FILE_EXTENSION, UML22UMLResource.Factory.INSTANCE);

        extensionFactoryMap.put("xml", UMLResource.Factory.INSTANCE);

        extensionFactoryMap.put("xml", new XMLResourceFactoryImpl());

        extensionFactoryMap.put("xmi", UMLResource.Factory.INSTANCE);
        extensionFactoryMap.put("xmi", new XMIResourceFactoryImpl());

        extensionFactoryMap.put("genmodel", new XMIResourceFactoryImpl());

    }


    public static void registerPackages(ResourceSet resourceSet) {
        Map<String, Object> packageRegistry = resourceSet.getPackageRegistry();
        packageRegistry.put(EcorePackage.eNS_URI, EcorePackage.eINSTANCE);
        packageRegistry.put(Ecore2XMLPackage.eNS_URI, Ecore2XMLPackage.eINSTANCE);
        packageRegistry.put(UMLPackage.eNS_URI, UMLPackage.eINSTANCE);
        packageRegistry.put(UML212UMLResource.UML_METAMODEL_NS_URI, UMLPackage.eINSTANCE);

        packageRegistry.put("http://www.eclipse.org/uml2/1.0.0/UML", UMLPackage.eINSTANCE);
        packageRegistry.put("http://www.eclipse.org/uml2/2.0.0/UML", UMLPackage.eINSTANCE);
        packageRegistry.put("http://www.eclipse.org/uml2/3.0.0/UML", UMLPackage.eINSTANCE);
        packageRegistry.put("http://www.eclipse.org/uml2/4.0.0/UML", UMLPackage.eINSTANCE);
        packageRegistry.put("http://www.eclipse.org/uml2/5.0.0/UML", UMLPackage.eINSTANCE);

        packageRegistry.put("http://www.omg.org/spec/UML/20061001", UMLPackage.eINSTANCE);
        packageRegistry.put("http://www.omg.org/spec/UML/20080501", UMLPackage.eINSTANCE);
        packageRegistry.put("http://www.omg.org/spec/UML/20101101", UMLPackage.eINSTANCE);
        packageRegistry.put("http://www.omg.org/spec/UML/20110701", UMLPackage.eINSTANCE);
        packageRegistry.put("http://www.omg.org/spec/UML/20131001", UMLPackage.eINSTANCE);

    }

    public static void registerPathmaps(URI uri) {
        URIConverter.URI_MAP.put(URI.createURI(UMLResource.LIBRARIES_PATHMAP),
                uri.appendSegment("libraries").appendSegment(""));
        URIConverter.URI_MAP.put(URI.createURI(UMLResource.METAMODELS_PATHMAP),
                uri.appendSegment("metamodels").appendSegment(""));
        URIConverter.URI_MAP.put(URI.createURI(UMLResource.PROFILES_PATHMAP),
                uri.appendSegment("profiles").appendSegment(""));

        URIConverter.URI_MAP.put(URI.createURI(UMLResource.LIBRARIES_PATHMAP),
                uri.appendSegment("libraries").appendSegment(""));

        URIConverter.URI_MAP.put(URI.createURI(UMLResource.METAMODELS_PATHMAP),
                uri.appendSegment("metamodels").appendSegment(""));

        URIConverter.URI_MAP.put(URI.createURI(UMLResource.UML_PRIMITIVE_TYPES_LIBRARY_URI),
                uri.appendSegment("libraries").appendSegment("UMLPrimitiveTypes.library.uml"));

        URIConverter.URI_MAP.put(URI.createURI(UMLResource.UML_METAMODEL_URI),
                uri.appendSegment("metamodels").appendSegment("UML.metamodel.uml"));

        URIConverter.URI_MAP.put(URI.createURI(UMLResource.UML2_PROFILE_URI),
                uri.appendSegment("profiles").appendSegment("UML2.profile.uml"));

    }

}

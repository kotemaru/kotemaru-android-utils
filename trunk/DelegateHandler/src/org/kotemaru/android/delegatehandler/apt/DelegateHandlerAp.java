package org.kotemaru.android.delegatehandler.apt;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.TypeElement;

import org.apache.velocity.VelocityContext;
import org.kotemaru.android.delegatehandler.annotation.DelegateHandler;

//@SupportedSourceVersion(SourceVersion.RELEASE_6)
@SupportedAnnotationTypes("org.kotemaru.android.delegatehandler.annotation.DelegateHandler")
// <-- Chenge it!!
public class DelegateHandlerAp extends ApBase
{
	private static String OUTPUT_PACKAGE_PATH = ".";
	private static String OUTPUT_CLASS_SUFFIX = "Handler";
	private static String TEMPLATE = DelegateHandlerAp.class.getCanonicalName().replace('.', '/')+".vm";

	public DelegateHandlerAp() {
	}

	@Override
	public boolean processClass(TypeElement classDecl) throws Exception {
		DelegateHandler anno = classDecl.getAnnotation(DelegateHandler.class);
		if (anno == null) return false;

		VelocityContext context = new VelocityContext();
		context.put("typeElement", classDecl);
		context.put("annotation", anno);
		context.put("classDecl", new ClassDecl(classDecl, environment));

		String pkgName = AptUtil.getPackageName(classDecl, OUTPUT_PACKAGE_PATH);
		String clsName = classDecl.getSimpleName() + OUTPUT_CLASS_SUFFIX;

		applyTemplate(context, pkgName, clsName, TEMPLATE);
		return true;
	}

	private final static Set<String> OPTIONS = new HashSet<String>();
	static {
	};

	@Override
	public Set<String> getSupportedOptions() {
		return OPTIONS;
	}

}

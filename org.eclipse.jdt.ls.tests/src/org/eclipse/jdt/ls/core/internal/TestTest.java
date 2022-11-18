/*******************************************************************************
 * Copyright (c) 2016-2017 Red Hat Inc. and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Red Hat Inc. - initial API and implementation
 *******************************************************************************/
package org.eclipse.jdt.ls.core.internal;

import static org.eclipse.jdt.ls.core.internal.WorkspaceHelper.getProject;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.CompletionProposal;
import org.eclipse.jdt.core.CompletionRequestor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.manipulation.CoreASTProvider;
import org.eclipse.jdt.ls.core.internal.JavaClientConnection;
import org.eclipse.jdt.ls.core.internal.JavaLanguageServerPlugin;
import org.eclipse.jdt.ls.core.internal.ProjectUtils;
import org.eclipse.jdt.ls.core.internal.ResourceUtils;
import org.eclipse.jdt.ls.core.internal.managers.*;
import org.eclipse.jdt.ls.core.internal.preferences.ClientPreferences;
import org.eclipse.lsp4j.FileSystemWatcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

class Requestor extends CompletionRequestor {
    public ArrayList<CompletionProposal> proposals = new ArrayList<>();

    @Override
    public void accept(CompletionProposal proposal) {
        // TODO Auto-generated method stub
        proposals.add(proposal);
    }
}

public class TestTest extends AbstractProjectsManagerBasedTest {

	private static final String BAR_PATTERN = "**/bar";
	private EclipseProjectImporter importer;

	@Before
	public void setUp() {
		importer = new EclipseProjectImporter();
	}

	@Test
	public void importSimpleJavaProject() throws Exception {
		String name = "myhello";
		importProjects("eclipse/"+name);
		IProject project = getProject(name);
		assertIsJavaProject(project);
		// a test for https://github.com/redhat-developer/vscode-java/issues/244
		importProjects("eclipse/" + name);
		project = getProject(name);
		// var cu = (ICompilationUnit) JavaCore.create(project.getFile(new Path("src/java/Foo.java")));
		// assertIsJavaProject(project);
        // // var javaProject = JavaCore.create(project);
        // // var children = javaProject.getChildren();
        // // var cu = javaProject.getPackageFragments()[0].getCompilationUnits()[0];
        // var requestor = new Requestor();
		var cu1 = (ICompilationUnit) JavaCore.create(project.getFile(new Path("src/java/Bar.java")));
		// var content = cu1.getSource();
        // cu.codeComplete(116, requestor);
		// var results = requestor.proposals.stream().map(x -> x.toString()).collect(Collectors.toList());
		// CoreASTProvider.getInstance().getAST(cu, CoreASTProvider.WAIT_YES, new NullProgressMonitor());
		CompilationUnit astRoot = CoreASTProvider.getInstance().getAST(cu1, CoreASTProvider.WAIT_YES, new NullProgressMonitor());
		var problems = astRoot.getProblems();
        // var unit = (ICompilktionUnit) javaProject.findElement(new Path("hello/src/java/Foo.java"));
	}
}


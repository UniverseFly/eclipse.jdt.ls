/*******************************************************************************
 * Copyright (c) 2019-2021 Microsoft Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Microsoft Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.jdt.ls.core.internal.handlers;

import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.NodeFinder;
import org.eclipse.jdt.core.manipulation.CoreASTProvider;
import org.eclipse.jdt.ls.core.internal.codemanipulation.OverrideMethodsOperation;
import org.eclipse.jdt.ls.core.internal.codemanipulation.OverrideMethodsOperation.OverridableMethod;
import org.eclipse.jdt.ls.core.internal.corrections.DiagnosticsHelper;
import org.eclipse.jdt.ls.core.internal.text.correction.SourceAssistProcessor;
import org.eclipse.lsp4j.CodeActionParams;
import org.eclipse.lsp4j.Range;
import org.eclipse.lsp4j.WorkspaceEdit;
import org.eclipse.text.edits.TextEdit;

public class OverrideMethodsHandler {

	public static OverridableMethodsResponse listOverridableMethods(CodeActionParams params, IProgressMonitor monitor) {
		IType type = SourceAssistProcessor.getSelectionType(params, monitor);
		String typeName = type == null ? "" : type.getTypeQualifiedName();
		List<OverridableMethod> methods = OverrideMethodsOperation.listOverridableMethods(type, monitor);
		return new OverridableMethodsResponse(typeName, methods);
	}

	public static WorkspaceEdit addOverridableMethods(AddOverridableMethodParams params, IProgressMonitor monitor) {
		IType type = SourceAssistProcessor.getSelectionType(params.context, monitor);
		if (type == null || params.context == null) {
			return null;
		}
		Range range = params.context.getRange();
		ASTNode declarationNode = null;
		CompilationUnit astRoot = CoreASTProvider.getInstance().getAST(type.getCompilationUnit(), CoreASTProvider.WAIT_YES, monitor);
		if (astRoot != null && range != null) {
			ASTNode node = NodeFinder.perform(astRoot, DiagnosticsHelper.getStartOffset(type.getCompilationUnit(), range), DiagnosticsHelper.getLength(type.getCompilationUnit(), range));
			declarationNode = SourceAssistProcessor.getTypeDeclarationNode(node);
		}
		// If cursor position is not specified, then insert to the last by default.
		IJavaElement insertPosition = (declarationNode != null) ? CodeGenerationUtils.findInsertElement(type, null) : CodeGenerationUtils.findInsertElement(type, range);
		TextEdit edit = OverrideMethodsOperation.addOverridableMethods(type, params.overridableMethods, insertPosition, monitor);
		if (edit == null) {
			return null;
		}

		return SourceAssistProcessor.convertToWorkspaceEdit(type.getCompilationUnit(), edit);
	}

	public static class OverridableMethodsResponse {
		public String type;
		public List<OverridableMethod> methods;

		public OverridableMethodsResponse() {

		}

		public OverridableMethodsResponse(String typeName, List<OverridableMethod> methods) {
			this.type = typeName;
			this.methods = methods;
		}
	}

	public static class AddOverridableMethodParams {
		public CodeActionParams context;
		public OverridableMethod[] overridableMethods;
	}
}

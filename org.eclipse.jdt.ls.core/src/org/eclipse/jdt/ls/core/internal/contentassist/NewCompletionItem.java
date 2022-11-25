package org.eclipse.jdt.ls.core.internal.contentassist;

public class NewCompletionItem {
    public final String type;
    public final String source;
    public final String target;
    public final int startOffset;
    public final int endOffset;

    public NewCompletionItem(
        String type,
        String source,
        String target,
        int startOffset,
        int endOffset
    ) {
        this.type = type;
        this.source = source;
        this.target = target;
        this.startOffset = startOffset;
        this.endOffset = endOffset;
    }
}
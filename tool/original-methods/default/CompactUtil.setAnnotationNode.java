private static void setAnnotationNode(FieldNode fn, CompactFieldNode cfn) {
    List<CompactAnnotationNode> annotations = new LinkedList<>();
    for (Object visibleAnnotation : fn.visibleAnnotations) {
        CompactAnnotationNode node = new CompactAnnotationNode();
        Map<String, Object> annotationMap = new HashMap<>();
        if (visibleAnnotation instanceof AnnotationNode) {
            AnnotationNode annotation = (AnnotationNode) visibleAnnotation;
            if (annotation.desc.contains("InputPortFieldAnnotation") || annotation.desc.contains("OutputPortFieldAnnotation")) {
                List<Object> annotationValues = annotation.values;
                if (annotationValues != null) {
                    int index = 0;
                    while (index <= annotationValues.size() - 2) {
                        String key = (String) annotationValues.get(index++);
                        Object value = annotationValues.get(index++);
                        annotationMap.put(key, value);
                    }
                    node.setAnnotations(annotationMap);
                    annotations.add(node);
                }
            }
        }
        cfn.setVisibleAnnotations(annotations);
    }
}
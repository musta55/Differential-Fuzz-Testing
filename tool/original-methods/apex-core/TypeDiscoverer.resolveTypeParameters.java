/**
 * Recursively resolve any type parameters against the type arguments in this context.
 * @param type
 * @param meta
 */
private void resolveTypeParameters(Type type, JSONObject meta) throws JSONException {
    if (type instanceof ParameterizedType) {
        ParameterizedType ptype = (ParameterizedType) type;
        JSONArray typeArgs = new JSONArray();
        for (Type argType : ptype.getActualTypeArguments()) {
            JSONObject argMeta = new JSONObject();
            resolveTypeParameters(argType, argMeta);
            typeArgs.put(argMeta);
        }
        meta.put("typeArgs", typeArgs);
        meta.put("type", ((Class<?>) ptype.getRawType()).getName());
        UI_TYPE uiType = UI_TYPE.getEnumFor((Class<?>) ptype.getRawType());
        if (uiType != null) {
            meta.put("uiType", uiType.getName());
        }
    } else if (type instanceof GenericArrayType) {
        GenericArrayType gat = (GenericArrayType) type;
        JSONArray typeArgs = new JSONArray();
        JSONObject argMeta = new JSONObject();
        Type componentType = gat.getGenericComponentType();
        componentType = this.typeArguments.get(componentType.toString());
        if (componentType == null) {
            componentType = gat.getGenericComponentType();
        }
        resolveTypeParameters(componentType, argMeta);
        typeArgs.put(argMeta);
        meta.put("typeArgs", typeArgs);
        if (componentType instanceof Class) {
            meta.put("type", Array.newInstance((Class<?>) componentType, 0).getClass().getName());
        } else {
            meta.put("type", Object[].class.getName());
        }
    } else if (type instanceof WildcardType) {
        meta.put("type", type);
        WildcardType wtype = (WildcardType) type;
        JSONObject wtMeta = new JSONObject();
        wtMeta.put("upper", getTypes(wtype.getUpperBounds()));
        wtMeta.put("lower", getTypes(wtype.getLowerBounds()));
        meta.put("typeBounds", wtMeta);
    } else {
        Type ta = this.typeArguments.get(type.toString());
        if (ta == null) {
            ta = type;
        }
        if (ta instanceof Class) {
            meta.put("type", ((Class<?>) ta).getName());
            UI_TYPE uiType = UI_TYPE.getEnumFor(((Class<?>) ta));
            if (uiType != null) {
                meta.put("uiType", uiType.getName());
            }
        } else if (ta instanceof ParameterizedType) {
            resolveTypeParameters(ta, meta);
        } else {
            meta.put("type", ta.toString());
        }
    }
}
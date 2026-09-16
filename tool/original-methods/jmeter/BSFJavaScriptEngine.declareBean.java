@Override
public void declareBean(BSFDeclaredBean bean) throws BSFException {
    if ((bean.bean instanceof Number) || (bean.bean == null) || (bean.bean instanceof String) || (bean.bean instanceof Boolean)) {
        global.put(bean.name, global, bean.bean);
    } else {
        // Must wrap non-scriptable objects before presenting to Rhino
        Scriptable wrapped = Context.toObject(bean.bean, global);
        global.put(bean.name, global, wrapped);
    }
}
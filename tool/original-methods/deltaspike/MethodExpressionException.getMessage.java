@Override
public String getMessage() {
    if (property != null) {
        return "Invalid property '" + property + "' in method expression " + repoClass.getName() + "." + method;
    }
    return "Method '" + method + "'of Repository " + repoClass.getName() + " is not a method expression";
}
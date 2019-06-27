package refer.classrefer;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.Type;
import model.Store;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MapVariNameReferRefactor {
    private static Pattern pattern=Pattern.compile("[a-zA-Z]*Map{1}<[A-Za-z0-9]*,\\s?[A-Za-z0-9]*>");
    public static void nameReferRefactor(String oldClassName,String newClassName){
        List<CompilationUnit> units= Store.javaFiles;
        for (CompilationUnit unit:units){
            List<VariableDeclarationExpr> variableDeclarationExprs = unit.findAll(VariableDeclarationExpr.class);
            if(!(variableDeclarationExprs.isEmpty()))
            for (VariableDeclarationExpr var : variableDeclarationExprs) {
                for(VariableDeclarator node:var.getVariables()) {
                    String name = node.getType().getClass().getName();
                    if(name.equals("com.github.javaparser.ast.type.ClassOrInterfaceType")) {
                        String className = node.getType().toString();
                        Matcher flag = judgeMap(className);
                        if (flag.find()) {
                            ClassOrInterfaceType type = (ClassOrInterfaceType) node.getType();
                            NodeList<Type> typeList = type.getTypeArguments().get();
                            NodeList<Type> resultList=new NodeList<>();
                            for (Type type1 : typeList) {
                                String typeName = type1.getClass().getName();
                                if (name.equals("com.github.javaparser.ast.type.ClassOrInterfaceType")) {
                                    ClassOrInterfaceType type2 = (ClassOrInterfaceType) type1;
                                    if (type2.getNameAsString().equals(oldClassName)) {
                                        type2.setName(newClassName);
                                        resultList.add(type2);
                                        continue;
                                    }
                                }
                                resultList.add(type1);
                            }
                            type.setTypeArguments(resultList);
                        }
                    }
                }}}
        }
    public static Matcher judgeMap(String name){
        Matcher m =pattern.matcher(name);
        return m;
    }
}

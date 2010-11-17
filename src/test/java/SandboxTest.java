//import com.mattinsler.cement.util.StringUtil;
//import net.sf.cglib.asm.Type;
//import net.sf.cglib.transform.ClassFilter;
//import net.sf.cglib.transform.ClassTransformer;
//import net.sf.cglib.transform.ClassTransformerFactory;
//import net.sf.cglib.transform.TransformingClassLoader;
//import net.sf.cglib.transform.impl.AbstractInterceptFieldCallback;
//import net.sf.cglib.transform.impl.InterceptFieldEnabled;
//import net.sf.cglib.transform.impl.InterceptFieldFilter;
//import net.sf.cglib.transform.impl.InterceptFieldTransformer;
//import org.junit.Test;
//
///**
// * Created by IntelliJ IDEA.
// * User: mattinsler
// * Date: 10/23/10
// * Time: 6:54 PM
// * To change this template use File | Settings | File Templates.
// */
//public class SandboxTest {
//    private static <T> Class<T> transformClass(final Class<T> target, final ClassTransformer transformer) throws ClassNotFoundException {
//        TransformingClassLoader loader = new TransformingClassLoader(target.getClassLoader(), new ClassFilter() {
//            public boolean accept(String name) {
//                System.out.println("ClassFilter: " + name + " (" + name.equals(target.getName()) + ")");
//                return name.equals(target.getName());
//            }
//        }, new ClassTransformerFactory() {
//            public ClassTransformer newInstance() {
//                return transformer;
//            }
//        });
//        return loader.loadClass(target.getName());
//    }
//
//    public static class Foo {
//        String bar;
//
//        @Override
//        public String toString() {
//            return bar;
//        }
//    }
//
//    @Test
//    public void testTransform() throws Exception {
//        ClassTransformer transformer = new InterceptFieldTransformer(
//                new InterceptFieldFilter() {
//                    public boolean acceptRead(Type type, String name) {
//                        System.out.println("InterceptFieldFilter.acceptRead[type:" + type + ", name:" + name + "]");
//                        return true;
//                    }
//                    public boolean acceptWrite(Type type, String name) {
//                        System.out.println("InterceptFieldFilter.acceptWriter[type:" + type + ", name:" + name + "]");
//                        return true;
//                    }
//                }
//        );
//
//        Class<Foo> c = transformClass(Foo.class, transformer);
//        System.out.println("interfaces: " + StringUtil.join(c.getInterfaces(), ", "));
//        InterceptFieldEnabled foo = (InterceptFieldEnabled)c.newInstance();
//
//        foo.setInterceptFieldCallback(new AbstractInterceptFieldCallback() {
//            @Override
//            public Object readObject(Object obj, String name, Object oldValue) {
//                System.out.println("InterceptFieldCallback.readObject[name:" + name + "]");
//                return super.readObject(obj, name, oldValue);
//            }
//        });
//
//        System.out.println((Foo)foo);
//
////        foo.bar = "Hello";
////        System.out.println(foo.bar);
//    }
//}

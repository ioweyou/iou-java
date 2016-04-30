package nl.brusque.iou.some_external_package;

import nl.brusque.iou.IOU;
import nl.brusque.iou.IThenCallable;
import org.junit.Test;

public class Examples {
    @Test
    public void testCallWithSingleThen() {
        IOU<Integer> iou = new IOU<>();

        iou.getPromise()
                .then(new IThenCallable<Integer, Void>() {
                    @Override
                    public Void apply(Integer input) throws Exception {
                        System.out.println(input.toString());

                        return null;
                    }
                });

        iou.resolve(42); // prints "42"

        allowTestToFinish();
    }

    @Test
    public void testCallWithPipedPromise() {
        IOU<Integer> iou = new IOU<>();

        iou.getPromise()
                .then(new IThenCallable<Integer, Integer>() {
                    @Override
                    public Integer apply(Integer input) throws Exception {
                        return input * 10;
                    }
                })
                .then(new IThenCallable<Integer, String>() {
                    @Override
                    public String apply(Integer input) throws Exception {
                        return String.format("The result: %d", input);
                    }
                })
                .then(new IThenCallable<String, Void>() {
                    @Override
                    public Void apply(String input) throws Exception {
                        System.out.println(input);

                        return null;
                    }
                });

        iou.resolve(42); // prints "The result: 420"

        allowTestToFinish();
    }

    @Test
    public void testCallReject() {
        IOU<Integer> iou = new IOU<>();

        iou.getPromise()
                .then(new IThenCallable<Integer, Integer>() {
                    @Override
                    public Integer apply(Integer integer) throws Exception {
                        return integer * 42;
                    }
                })
                .fail(new IThenCallable<Object, Void>() {
                    @Override
                    public Void apply(Object input) throws Exception {
                        System.out.println(String.format("%s I can't do that.", input));

                        return null;
                    }
                });

        iou.reject("I'm sorry, Dave."); // prints "I'm sorry, Dave. I can't do that."

        allowTestToFinish();
    }

    @Test
    public void testCallRejectAPlus() {
        IOU<Integer> iou = new IOU<>();

        iou.getPromise()
                .then(new IThenCallable<Integer, Integer>() {
                    @Override
                    public Integer apply(Integer input) throws Exception {
                        return input * 42;
                    }
                }, new IThenCallable<Object, Integer>() {
                    @Override
                    public Integer apply(Object input) throws Exception {
                        System.out.println(String.format("%s I can't do that.", input));

                        return null;
                    }
                });

        iou.reject("I'm sorry, A+."); // prints "I'm sorry, A+. I can't do that."

        allowTestToFinish();
    }

    @Test
    public void testCallFail() {
        IOU<Integer> iou = new IOU<>();

        iou.getPromise()
                .then(new IThenCallable<Integer, Integer>() {
                    @Override
                    public Integer apply(Integer input) throws Exception {
                        throw new Exception("I just don't care.");
                    }
                })
                .then(new IThenCallable<Integer, Void>() {
                    @Override
                    public Void apply(Integer input) throws Exception {
                        System.out.println("What would you say you do here?");

                        return null;
                    }
                })
                .fail(new IThenCallable<Object, Void>() {
                    @Override
                    public Void apply(Object reason) throws Exception {
                        System.out.println(
                            String.format("It's not that I'm lazy, it's that %s",
                                    ((Exception)reason).getMessage()));

                        return null;
                    }
                });

        iou.resolve(42); // prints "It's not that I'm lazy, it's that I just don't care."

        allowTestToFinish();
    }

    private void allowTestToFinish() {
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
package nl.brusque.iou.some_external_package;

import nl.brusque.iou.IOU;
import nl.brusque.iou.IThenCallable;
import nl.brusque.iou.Promise;
import org.junit.Assert;
import org.junit.Test;

public class PromiseInterfaceTests {
    private final int DUMMY_INTEGER  = 1;
    private final Error DUMMY_REASON = new Error("DUMMY");

    @Test
    public void testSimpleCall() {
        final int[] _result = {0};

        IOU<Integer> iou = new IOU<>();

        iou.getPromise().then(new IThenCallable<Integer, Void>() {
            @Override
            public Void apply(Integer integer) throws Exception {
                _result[0] = integer;

                return null;
            }
        });

        iou.resolve(DUMMY_INTEGER);
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Assert.assertEquals("Result does not equal DUMMY_INTEGER", DUMMY_INTEGER, _result[0]);
    }

    @Test
    public void testWhenCall() {
        final int[] _result = {0};

        IOU<Integer> iou = new IOU<>();

        iou.when(1)
            .then(new IThenCallable<Integer, Object>() {
                    @Override
                    public Object apply(Integer integer) throws Exception {
                    _result[0] = integer;

                        return null;
                    }
            });

        iou.resolve(DUMMY_INTEGER);
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Assert.assertEquals("Result does not equal DUMMY_INTEGER", DUMMY_INTEGER, _result[0]);
    }

    @Test
    public void testCallWithFailure() {
        final boolean[] _result = {false};

        IOU<Integer> iou = new IOU<>();

        iou.getPromise()
            .then(new IThenCallable<Integer, Void>() {
                @Override
                public Void apply(Integer integer) throws Exception {
                    throw new Exception("This is an exception");
                }
            })
            .fail(new IThenCallable<Object, Void>() {
                @Override
                public Void apply(Object o) throws Exception {
                    _result[0] = true;

                    return null;
                }
            });

        iou.resolve(DUMMY_INTEGER);
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Assert.assertTrue("Fail-branch was not called", _result[0]);
    }

    @Test
    public void testChainedCall() {
        final int[] _firstBanchResult = {0};
        final int[] _secondBanchResult = {0};

        IOU<Integer> iou = new IOU<>();

        iou.getPromise()
            .then(new IThenCallable<Integer, Integer>() {
                @Override
                public Integer apply(Integer integer) throws Exception {
                    _firstBanchResult[0] = integer;

                    return integer * 42;
                }
            })
            .then(new IThenCallable<Integer, Void>() {
                @Override
                public Void apply(Integer integer) throws Exception {
                    _secondBanchResult[0] = integer;

                    return null;
                }
            });

        iou.resolve(DUMMY_INTEGER);
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Assert.assertEquals("First branch was not called with correct value", DUMMY_INTEGER, _firstBanchResult[0]);
        Assert.assertEquals("Second branch was not called with correct value", DUMMY_INTEGER * 42, _secondBanchResult[0]);
    }

    @Test
    public void testParallelCall() {
        final int[] _firstBanchResult = {0};
        final int[] _secondBanchResult = {0};

        IOU<Integer> iou = new IOU<>();

        Promise<Integer> p = iou.getPromise();
        p.then(new IThenCallable<Integer, Integer>() {
            @Override
            public Integer apply(Integer integer) throws Exception {
                _firstBanchResult[0] = integer;

                return integer * 42;
            }
        });
        p.then(new IThenCallable<Integer, Void>() {
            @Override
            public Void apply(Integer integer) throws Exception {
                _secondBanchResult[0] = integer;

                return null;
            }
        });

        iou.resolve(DUMMY_INTEGER);
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Assert.assertEquals("First branch was not called with correct value", DUMMY_INTEGER, _firstBanchResult[0]);
        Assert.assertEquals("Second branch was not called with correct value", DUMMY_INTEGER, _secondBanchResult[0]);
    }

    @Test
    public void testRoberPaulson() {
        IOU<Integer> iou = new IOU<>();

        iou.getPromise()
                .then(new IThenCallable<Integer, Integer>() {
                    @Override
                    public Integer apply(Integer input) throws Exception {
                        throw new Exception("I feel like destroying something beautiful");
                    }
                })
                .then(new IThenCallable<Integer, Void>() {
                    @Override
                    public Void apply(Integer somethingBeautiful) throws Exception {
                        System.out.println(String.format("This is beautiful: %d", somethingBeautiful));

                        return null;
                    }
                })
                .fail(new IThenCallable<Object, Void>() {
                    @Override
                    public Void apply(Object reason) throws Exception {
                        System.out.println(String.format("The promise was rejected, because %s", ((Exception)reason).getMessage()));

                        return null;
                    }
                });

        iou.resolve(42); // prints "The promise was rejected, because I feel like destroying something beautiful"
    }

    @Test
    public void testThenWithFulfillAndRejectThenable() {
        final int[] _fulfillBranchResult = {0};
        final Object[] _rejectBranchResult = {0};

        IOU<Integer> iou = new IOU<>();

        Promise<Integer> p = iou.getPromise();
        p.then(new IThenCallable<Integer, Integer>() {
            @Override
            public Integer apply(Integer integer) throws Exception {
                _fulfillBranchResult[0] = integer;

                return integer;
            }
        }, new IThenCallable<Object, Integer>() {
            @Override
            public Integer apply(Object reason) throws Exception {
                _rejectBranchResult[0] = reason;

                return DUMMY_INTEGER;
            }
        });

        iou.reject(DUMMY_REASON);
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Assert.assertEquals("Fulfill-branch should not have been called", 0, _fulfillBranchResult[0]);
        Assert.assertEquals("Reject-branch was not called with correct value", DUMMY_REASON, _rejectBranchResult[0]);
    }
}

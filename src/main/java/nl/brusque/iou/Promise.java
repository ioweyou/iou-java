package nl.brusque.iou;

public class Promise<TFulfill> extends AbstractPromise<TFulfill> {
    Promise() { super(); }

    @Override
    protected <TAnythingFulfill> Promise<TAnythingFulfill> create() {
        return new Promise<>();
    }

    public <TAnythingOutput> IThenable<TAnythingOutput> fail(IThenCallable<Object, TAnythingOutput> failureCallable) {
        return super.then(null, failureCallable);
    }

    public <TAnythingOutput> Promise<TAnythingOutput> then(IThenCallable<TFulfill, TAnythingOutput> onFulfilled) {
        return (Promise<TAnythingOutput>)super.then(onFulfilled);
    }

    public <TAnythingOutput> Promise<TAnythingOutput> then(IThenCallable<TFulfill, TAnythingOutput> onFulfilled, IThenCallable<Object, TAnythingOutput> onRejected) {
        return (Promise<TAnythingOutput>)super.then(onFulfilled, onRejected);
    }
}

package nl.brusque.iou;

public class IOU<TFulfill> extends AbstractIOU<TFulfill> {
    private final Promise<TFulfill> _promise = new Promise<>();

    @Override
    public Promise<TFulfill> getPromise() {
        return _promise;
    }

    public Promise<TFulfill> when(TFulfill o) {
        resolve(o);

        return _promise;
    }
}

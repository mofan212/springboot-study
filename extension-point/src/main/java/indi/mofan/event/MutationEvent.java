package indi.mofan.event;

import indi.mofan.enums.MutationType;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import org.springframework.core.ResolvableType;
import org.springframework.core.ResolvableTypeProvider;

/**
 * @author mofan
 * @date 2022/10/17 21:21
 */
@Getter
public class MutationEvent<T> extends ApplicationEvent implements ResolvableTypeProvider {

    private static final long serialVersionUID = -2718823625228147843L;

    private final T source;

    private final MutationType type;

    public MutationEvent(T data, MutationType type) {
        super(data);
        this.source = data;
        this.type = type;
    }

    @Override
    public ResolvableType getResolvableType() {
        return ResolvableType.forClassWithGenerics(getClass(),
                ResolvableType.forInstance(this.source));
    }
}
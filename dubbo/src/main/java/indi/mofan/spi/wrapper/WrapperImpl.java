package indi.mofan.spi.wrapper;

import lombok.Getter;

/**
 * @author mofan
 * @date 2023/3/14 11:26
 */
@Getter
public class WrapperImpl implements MofanSpi {
    private final MofanSpi mofanSpi;

    public WrapperImpl(MofanSpi mofanSpi) {
        this.mofanSpi = mofanSpi;
    }
}

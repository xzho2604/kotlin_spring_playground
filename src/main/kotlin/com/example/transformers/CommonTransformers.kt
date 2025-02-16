package com.example.transformers

import org.springframework.stereotype.Component


@Component("noOpTransformer")
class NoOpTransformer<I, O> : Transformer<I, O> {
    override fun transform(input: I): O {
        return input as O
    }
}
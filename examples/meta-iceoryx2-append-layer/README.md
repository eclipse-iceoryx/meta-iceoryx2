# Customize the iceoryx2 yocto recipes

In order to customize the iceoryx2 build, a `meta-iceoryx2-append-layer` can be
created. In its `conf/layer.conf`, the `ICEORYX2_VERSION` can be set to the
desired version, if it deviates from the preferred version of the git tag.

The `recipes/iceoryx2*.bbappend` files can be used to set additional flags,
e.g. `CARGO_FEATURES:append = ",iceoryx2/dev_permissions"`.

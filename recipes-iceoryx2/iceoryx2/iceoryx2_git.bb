SUMMARY = "iceoryx2"
LICENSE = "Apache-2.0 | MIT"
LIC_FILES_CHKSUM = "file://LICENSE-APACHE;md5=22a53954e4e0ec258dfce4391e905dac \
                    file://LICENSE-MIT;md5=b377b220f43d747efdec40d69fcaa69d"

# Enable network for the compile task allowing cargo to download dependencies
do_compile[network] = "1"

# zlib and expath due to python ... maybe just add python ... should be build dependency
DEPENDS = " zlib expat python3"

SRC_URI = "git://github.com/eclipse-iceoryx/iceoryx2.git;protocol=https;branch=main"
SRCREV = "70c925416482c821df04d564e3b8ecf91cd1191c"

S = "${WORKDIR}/git"

INSANE_SKIP:${PN} += " already-stripped"
FILES_SOLIBSDEV = ""

# This should actually be handled by meta-rust-bin most likely
# in classes/cargo_bin.bbclass similar to Yocto Poky
# meta/classes-recipe/rust-common.bbclass populating RUSTFLAGS
# to prevent bitbake warnings "contains reference to TMPDIR [buildpaths]"
# At this time, meta-rust-bin allows to pass in additional flags
# via EXTRA_RUSTFLAGS to populate RUSTFLAGS
RUST_DEBUG_REMAP = "--remap-path-prefix=${WORKDIR}=${TARGET_DBGSRC_DIR}"
EXTRA_RUSTFLAGS = "${RUST_DEBUG_REMAP}"

inherit cargo_bin

# See https://github.com/rust-embedded/meta-rust-bin/blob/master/classes/cargo_bin.bbclass for variables to control the compilations
CARGO_FEATURES = "libc_platform"
EXTRA_CARGO_FLAGS = " --tests --workspace --all-targets --exclude iceoryx2-ffi-python"
# EXTRA_CARGO_FLAGS = " --tests --examples"

# TODO
# license location: https://github.com/rust-embedded/meta-rust-bin/tree/master/files/common-licenses
# versioned recipes: https://github.com/rust-embedded/meta-rust-bin/tree/master/recipes-devtools/rust

IOX2_STAGING_DIR = "${STAGING_DIR}/iceoryx2-ffi-artifacts"

do_install() {
    install -d ${IOX2_STAGING_DIR}
    install -m 0755 ${CARGO_BINDIR}/libiceoryx2_ffi.a ${IOX2_STAGING_DIR}
    install -m 0755 ${CARGO_BINDIR}/libiceoryx2_ffi.so ${IOX2_STAGING_DIR}
    
    install -d ${IOX2_STAGING_DIR}/iceoryx2-ffi-cbindgen/include/iox2
    install -m 0755 ${CARGO_BINDIR}/iceoryx2-ffi-cbindgen/include/iox2/iceoryx2.h ${IOX2_STAGING_DIR}/iceoryx2-ffi-cbindgen/include/iox2

    install -d ${IOX2_STAGING_DIR}/cli
    install -m 0755 ${CARGO_BINDIR}/iox2 ${IOX2_STAGING_DIR}/cli
    install -m 0755 ${CARGO_BINDIR}/iox2-config ${IOX2_STAGING_DIR}/cli
    install -m 0755 ${CARGO_BINDIR}/iox2-node ${IOX2_STAGING_DIR}/cli
    install -m 0755 ${CARGO_BINDIR}/iox2-service ${IOX2_STAGING_DIR}/cli
    install -m 0755 ${CARGO_BINDIR}/iox2-tunnel ${IOX2_STAGING_DIR}/cli
    
    install -d ${IOX2_STAGING_DIR}/benchmarks
    install -m 0755 ${CARGO_BINDIR}/benchmark-event ${IOX2_STAGING_DIR}/benchmarks
    install -m 0755 ${CARGO_BINDIR}/benchmark-publish-subscribe ${IOX2_STAGING_DIR}/benchmarks
    install -m 0755 ${CARGO_BINDIR}/benchmark-queue ${IOX2_STAGING_DIR}/benchmarks
    install -m 0755 ${CARGO_BINDIR}/benchmark-request-response ${IOX2_STAGING_DIR}/benchmarks

    install -d ${IOX2_STAGING_DIR}/examples
    install -m 0755 ${CARGO_BINDIR}/examples/publish_subscribe_publisher ${IOX2_STAGING_DIR}/examples
    install -m 0755 ${CARGO_BINDIR}/examples/publish_subscribe_subscriber ${IOX2_STAGING_DIR}/examples

    install -d ${IOX2_STAGING_DIR}/tests
    install -m 0755 ${CARGO_BINDIR}/deps/vec_tests-* ${IOX2_STAGING_DIR}/tests
}

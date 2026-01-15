SUMMARY = "iceoryx2"
DESCRIPTION = "This package is used to build the iceoryx2 benchmarks, examples and tests"
HOMEPAGE = "https://iceoryx.io"
BUGTRACKER = "https://github.com/eclipse-iceoryx/iceoryx2/issues"
LICENSE = "Apache-2.0 | MIT"
LIC_FILES_CHKSUM = "file://LICENSE-APACHE;md5=22a53954e4e0ec258dfce4391e905dac \
                    file://LICENSE-MIT;md5=b377b220f43d747efdec40d69fcaa69d"

# Enable network for the compile task allowing cargo to download dependencies
do_compile[network] = "1"

DEPENDS = ""

SRC_URI = "git://github.com/eclipse-iceoryx/iceoryx2.git;protocol=https;nobranch=1 \
           file://run-ptest"

S = "${WORKDIR}/git"

inherit cargo_bin ptest

INSANE_SKIP:${PN} += "already-stripped"
FILES_SOLIBSDEV = ""

# This should actually be handled by meta-rust-bin most likely
# in classes/cargo_bin.bbclass similar to Yocto Poky
# meta/classes-recipe/rust-common.bbclass populating RUSTFLAGS
# to prevent bitbake warnings "contains reference to TMPDIR [buildpaths]"
# At this time, meta-rust-bin allows to pass in additional flags
# via EXTRA_RUSTFLAGS to populate RUSTFLAGS
RUST_DEBUG_REMAP = "--remap-path-prefix=${WORKDIR}=${TARGET_DBGSRC_DIR}"
# See https://github.com/rust-embedded/meta-rust-bin/blob/master/classes/cargo_bin.bbclass
# for variables to control the compilations
EXTRA_RUSTFLAGS = "${RUST_DEBUG_REMAP}"
CARGO_FEATURES = "iceoryx2/libc_platform"
EXTRA_CARGO_FLAGS = "--tests --workspace --all-targets --exclude iceoryx2-ffi-python --exclude component-tests_rust --exclude iceoryx2_tunnel_end_to_end_tests"

BBCLASSEXTEND = "native nativesdk"

PACKAGES =+ "iceoryx2-benchmarks iceoryx2-examples"

do_install() {
    install -d ${D}${bindir}/iceoryx2/benchmarks
    install -m 0755 ${CARGO_BINDIR}/benchmark-event ${D}${bindir}/iceoryx2/benchmarks
    install -m 0755 ${CARGO_BINDIR}/benchmark-publish-subscribe ${D}${bindir}/iceoryx2/benchmarks
    install -m 0755 ${CARGO_BINDIR}/benchmark-queue ${D}${bindir}/iceoryx2/benchmarks
    install -m 0755 ${CARGO_BINDIR}/benchmark-request-response ${D}${bindir}/iceoryx2/benchmarks

    install -d ${D}${bindir}/iceoryx2/examples/rust
    for example in ${CARGO_BINDIR}/examples/*; do
        if [ -f "$example" ] && [ -x "$example" ] && [ "${example##*.}" != "so" ]; then
            install -m 0755 "$example" ${D}${bindir}/iceoryx2/examples/rust
        fi
    done
}

do_install_ptest() {
    install -d ${D}/${PTEST_PATH}/tests
    for test in ${CARGO_BINDIR}/deps/*; do
        if [ -f "$test" ] && [ -x "$test" ] && [ "${test##*.}" != "so" ]; then
            case "$test" in
                *benchmark*|*component-tests_rust*|*macro*|*tunnel_end_to_end*) continue ;;
            esac
            install -m 0755 "$test" ${D}/${PTEST_PATH}/tests
        fi
    done
    install -m 0755 ${S}/../run-ptest ${D}/${PTEST_PATH}/
}

SUMMARY:iceoryx2-benchmarks = "The iceoryx2 benchmarks"
DESCRIPTION:iceoryx2-benchmarks = "This package contains the iceoryx2 benchmarks. \
                                They are available in '/usr/bin/iceoryx2/benchmarks'"
HOMEPAGE:iceoryx2-benchmarks = "https://iceoryx.io"
BUGTRACKER:iceoryx2-benchmarks = "https://github.com/eclipse-iceoryx/iceoryx2/issues"
FILES:iceoryx2-benchmarks += "${bindir}/iceoryx2/benchmarks/*"

SUMMARY:iceoryx2-examples = "The iceoryx2 Rust examples"
DESCRIPTION:iceoryx2-examples = "This package contains the iceoryx2 Rust examples. \
                              They are available in '/usr/bin/iceoryx2/examples'"
HOMEPAGE:iceoryx2-examples = "https://iceoryx.io"
BUGTRACKER:iceoryx2-examples = "https://github.com/eclipse-iceoryx/iceoryx2/issues"
FILES:iceoryx2-examples += "${bindir}/iceoryx2/examples/rust/*"

# NOTE bitbake complains about nobody providing iceoryx2-validation-suite needed by
#      iceoryx2-validation-suite-ptest; since iceoryx2-validation-suite is an empty
#      package, it will not be generated -> remove it from the dependencies
RDEPENDS:iceoryx2-validation-suite-ptest:remove = "iceoryx2-validation-suite"

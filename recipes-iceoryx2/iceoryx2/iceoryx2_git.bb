SUMMARY = "iceoryx2"
DESCRIPTION = "This package is used to build the iceoryx2 Rust artifacts"
HOMEPAGE = "https://iceoryx.io"
BUGTRACKER = "https://github.com/eclipse-iceoryx/iceoryx2/issues"
LICENSE = "Apache-2.0 | MIT"
LIC_FILES_CHKSUM = "file://LICENSE-APACHE;md5=22a53954e4e0ec258dfce4391e905dac \
                    file://LICENSE-MIT;md5=b377b220f43d747efdec40d69fcaa69d"

# Enable network for the compile task allowing cargo to download dependencies
do_compile[network] = "1"

DEPENDS = ""

SRC_URI = "git://github.com/eclipse-iceoryx/iceoryx2.git;protocol=https;branch=main \
           file://run-ptest"
SRCREV = "40685355dc335f5507b1f505679724fd9f76e035"

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
CARGO_FEATURES = "libc_platform"
EXTRA_CARGO_FLAGS = "--tests --workspace --all-targets --exclude iceoryx2-ffi-python"

BBCLASSEXTEND = "native nativesdk"

PACKAGES =+ "${PN}-cli ${PN}-benchmarks ${PN}-examples ${PN}-tests"

IOX2_STAGING_DIR = "${STAGING_DIR}/iceoryx2-artifacts"

do_install() {
    install -d ${IOX2_STAGING_DIR}
    install ${CARGO_BINDIR}/libiceoryx2_ffi_c.a ${IOX2_STAGING_DIR}
    install -m 0755 ${CARGO_BINDIR}/libiceoryx2_ffi_c.so ${IOX2_STAGING_DIR}

    install -d ${IOX2_STAGING_DIR}/iceoryx2-ffi-c-cbindgen/include/iox2
    install ${CARGO_BINDIR}/iceoryx2-ffi-c-cbindgen/include/iox2/iceoryx2.h ${IOX2_STAGING_DIR}/iceoryx2-ffi-c-cbindgen/include/iox2

    install -d ${D}${bindir}
    install -m 0755 ${CARGO_BINDIR}/iox2 ${D}${bindir}
    install -m 0755 ${CARGO_BINDIR}/iox2-config ${D}${bindir}
    install -m 0755 ${CARGO_BINDIR}/iox2-node ${D}${bindir}
    install -m 0755 ${CARGO_BINDIR}/iox2-service ${D}${bindir}
    install -m 0755 ${CARGO_BINDIR}/iox2-tunnel ${D}${bindir}

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
                *benchmark*|*macro*|*tunnels_end_to_end*) continue ;;
            esac
            install -m 0755 "$test" ${D}/${PTEST_PATH}/tests
        fi
    done
    install -m 0755 ${S}/../run-ptest ${D}/${PTEST_PATH}/
}

SUMMARY:${PN}-cli = "The iceoryx2 command line tools"
DESCRIPTION:${PN}-cli = "This package contains the iceoryx2 command line tools. \
                         Use 'iox2 --list' to show a list of all available commands."
HOMEPAGE:${PN}-cli = "https://iceoryx.io"
BUGTRACKER:${PN}-cli = "https://github.com/eclipse-iceoryx/iceoryx2/issues"
FILES:${PN}-cli += "${bindir}/iox2"
FILES:${PN}-cli += "${bindir}/iox2-config"
FILES:${PN}-cli += "${bindir}/iox2-node"
FILES:${PN}-cli += "${bindir}/iox2-service"
FILES:${PN}-cli += "${bindir}/iox2-tunnel"

SUMMARY:${PN}-benchmarks = "The iceoryx2 benchmarks"
DESCRIPTION:${PN}-benchmarks = "This package contains the iceoryx2 benchmarks. \
                                They are available in '/usr/bin/iceoryx2/benchmarks'"
HOMEPAGE:${PN}-benchmarks = "https://iceoryx.io"
BUGTRACKER:${PN}-benchmarks = "https://github.com/eclipse-iceoryx/iceoryx2/issues"
FILES:${PN}-benchmarks += "${bindir}/iceoryx2/benchmarks/*"

SUMMARY:${PN}-examples = "The iceoryx2 Rust examples"
DESCRIPTION:${PN}-examples = "This package contains the iceoryx2 Rust examples. \
                              They are available in '/usr/bin/iceoryx2/examples'"
HOMEPAGE:${PN}-examples = "https://iceoryx.io"
BUGTRACKER:${PN}-examples = "https://github.com/eclipse-iceoryx/iceoryx2/issues"
FILES:${PN}-examples += "${bindir}/iceoryx2/examples/rust/*"

RDEPENDS:${PN}-ptest:remove = "iceoryx2"

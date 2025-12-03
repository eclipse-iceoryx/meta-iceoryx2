SUMMARY = "iceoryx2"
DESCRIPTION = "This package is used to build iceoryx2-cli and iceoryx2-ffi-c"
HOMEPAGE = "https://iceoryx.io"
BUGTRACKER = "https://github.com/eclipse-iceoryx/iceoryx2/issues"
LICENSE = "Apache-2.0 | MIT"
LIC_FILES_CHKSUM = "file://LICENSE-APACHE;md5=22a53954e4e0ec258dfce4391e905dac \
                    file://LICENSE-MIT;md5=b377b220f43d747efdec40d69fcaa69d"

# Enable network for the compile task allowing cargo to download dependencies
do_compile[network] = "1"

DEPENDS = ""

SRC_URI = "git://github.com/eclipse-iceoryx/iceoryx2.git;protocol=https;branch=main"

S = "${WORKDIR}/git"

inherit cargo_bin

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
EXTRA_CARGO_FLAGS = " --package iceoryx2-ffi-c --package iceoryx2-cli"

BBCLASSEXTEND = "native nativesdk"

PACKAGES =+ "${PN}-cli"

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

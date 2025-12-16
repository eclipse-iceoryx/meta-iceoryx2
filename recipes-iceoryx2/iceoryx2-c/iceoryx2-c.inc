SUMMARY = "iceoryx2 C bindings"
DESCRIPTION = "This package contains the iceoryx2 C bindings"
HOMEPAGE = "https://iceoryx.io"
BUGTRACKER = "https://github.com/eclipse-iceoryx/iceoryx2/issues"
LICENSE = "Apache-2.0 | MIT"
LIC_FILES_CHKSUM = "file://LICENSE-APACHE;md5=22a53954e4e0ec258dfce4391e905dac \
                    file://LICENSE-MIT;md5=b377b220f43d747efdec40d69fcaa69d"

inherit cmake

DEPENDS = "iceoryx2 iceoryx2-cmake-modules"

SRC_URI = "git://github.com/eclipse-iceoryx/iceoryx2.git;protocol=https;nobranch=1"

S = "${WORKDIR}/git/iceoryx2-c"

INSANE_SKIP:${PN} += "already-stripped"
FILES_SOLIBSDEV = ""

IOX2_STAGING_DIR = "${STAGING_DIR}/iceoryx2-artifacts"
EXTRA_OECMAKE += "-DRUST_BUILD_ARTIFACT_PATH=${IOX2_STAGING_DIR}"
EXTRA_OECMAKE += "-DCMAKE_INSTALL_PREFIX=${D}/${exec_prefix}"

FILES_${PN}-staticdev += "${libdir}/libiceoryx2_ffi_c.a"
FILES:${PN} += "${libdir}/libiceoryx2_ffi_c.so"
RDEPENDS_${PN}-dev += "${PN}-staticdev"
BBCLASSEXTEND = "native nativesdk"

do_configure[depends] += "iceoryx2:do_configure"
do_compile[depends] += "iceoryx2:do_compile"
do_install[depends] += "iceoryx2:do_install"

do_install() {
  cmake --install ${S}/../../build
}

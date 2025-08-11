SUMMARY = "iceoryx2bindings"
LICENSE = "Apache-2.0 | MIT"
LIC_FILES_CHKSUM = "file://LICENSE-APACHE;md5=22a53954e4e0ec258dfce4391e905dac \
                    file://LICENSE-MIT;md5=b377b220f43d747efdec40d69fcaa69d"

inherit cmake

DEPENDS = " iceoryx-hoofs iceoryx2"

SRC_URI = "git://github.com/eclipse-iceoryx/iceoryx2.git;protocol=https;branch=main"
SRCREV = "70c925416482c821df04d564e3b8ecf91cd1191c"

S = "${WORKDIR}/git"

INSANE_SKIP:${PN} += " already-stripped"
FILES_SOLIBSDEV = ""

# TODO rename iceoryx2-ffi-artifacts when als the examples and tests are placed there
IOX2_STAGING_DIR = "${STAGING_DIR}/iceoryx2-ffi-artifacts"
EXTRA_OECMAKE += " -DRUST_BUILD_ARTIFACT_PATH=${IOX2_STAGING_DIR}"
EXTRA_OECMAKE += " -DCMAKE_INSTALL_PREFIX=${D}/${exec_prefix}"

FILES_${PN}-staticdev += "${libdir}/libiceoryx2_ffi.a"
FILES_${PN}-staticdev += "${libdir}/libiceoryx2_cxx.a"
FILES:${PN} += "${libdir}/libiceoryx2_ffi.so"
FILES:${PN} += "${libdir}/libiceoryx2_cxx.so"
RDEPENDS_${PN}-dev += "${PN}-staticdev"
BBCLASSEXTEND = "native nativesdk"

do_install() {
  cmake --install ${S}/../build

  install -d ${D}${bindir}/cli
  install ${IOX2_STAGING_DIR}/cli/* ${D}${bindir}/cli

  install -d ${D}${bindir}/benchmarks
  install ${IOX2_STAGING_DIR}/benchmarks/* ${D}${bindir}/benchmarks

  install -d ${D}${bindir}/examples
  install ${IOX2_STAGING_DIR}/examples/* ${D}${bindir}/examples

#  install -d ${D}${bindir}/tests
#  install ${IOX2_STAGING_DIR}/tests/* ${D}${bindir}/tests
}

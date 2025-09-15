SUMMARY = "iceoryx2 C++ bindings"
DESCRIPTION = "This package contains the iceoryx2 C++ bindings"
HOMEPAGE = "https://iceoryx.io"
BUGTRACKER = "https://github.com/eclipse-iceoryx/iceoryx2/issues"
LICENSE = "Apache-2.0 | MIT"
LIC_FILES_CHKSUM = "file://LICENSE-APACHE;md5=22a53954e4e0ec258dfce4391e905dac \
                    file://LICENSE-MIT;md5=b377b220f43d747efdec40d69fcaa69d"

inherit cmake ptest

# NOTE:: googletest requires meta-openembedded/meta-oe
DEPENDS = " \
  iceoryx-hoofs-subset \
  iceoryx2-c \
  iceoryx2-cmake-modules \
  googletest \
  "

SRC_URI = "git://github.com/eclipse-iceoryx/iceoryx2.git;protocol=https;branch=main \
          file://run-ptest"
SRCREV = "b4042aa2aafe5f0d613a263e7cd4bbf8afb4a34e"

S = "${WORKDIR}/git/iceoryx2-cxx"

INSANE_SKIP:${PN} += "already-stripped"
FILES_SOLIBSDEV = ""

EXTRA_OECMAKE += "-DCMAKE_INSTALL_PREFIX=${D}/${exec_prefix} -DBUILD_TESTING=ON -DUSE_SYSTEM_GTEST=ON"

PACKAGES =+ "${PN}-tests"
FILES:${PN}-staticdev += "${libdir}/libiceoryx2_cxx.a"
FILES:${PN} += "${libdir}/libiceoryx2_cxx.so"
RDEPENDS:${PN}-dev += "${PN}-staticdev iceoryx-hoofs-subset-dev"
RDEPENDS:${PN} += "iceoryx-hoofs-subset iceoryx2-c"
BBCLASSEXTEND = "native nativesdk"

do_install() {
  cmake --install ${S}/../../build
}

do_install_ptest() {
    install -d ${D}/${PTEST_PATH}/tests
    install -m 0755  ${B}/tests/iceoryx2-cxx-tests ${D}/${PTEST_PATH}/tests
    install -m 0755  ${S}/../../run-ptest ${D}/${PTEST_PATH}/
}


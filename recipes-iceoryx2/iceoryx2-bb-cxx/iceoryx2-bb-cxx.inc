SUMMARY = "iceoryx2 BB C++"
DESCRIPTION = "This package contains the iceoryx2 C++ Building Blocks"
HOMEPAGE = "https://iceoryx.io"
BUGTRACKER = "https://github.com/eclipse-iceoryx/iceoryx2/issues"
LICENSE = "Apache-2.0 | MIT"
LIC_FILES_CHKSUM = "file://LICENSE-APACHE;md5=22a53954e4e0ec258dfce4391e905dac \
                    file://LICENSE-MIT;md5=b377b220f43d747efdec40d69fcaa69d"

inherit cmake ptest

# NOTE:: googletest requires meta-openembedded/meta-oe
DEPENDS = " \
  iceoryx2-cmake-modules \
  googletest \
  "

SRC_URI = "git://github.com/eclipse-iceoryx/iceoryx2.git;protocol=https;nobranch=1 \
          file://run-ptest"

S = "${WORKDIR}/git/iceoryx2-bb/cxx"

INSANE_SKIP:${PN} += "already-stripped"

EXTRA_OECMAKE += "-DCMAKE_INSTALL_PREFIX=${D}/${exec_prefix} -DBUILD_TESTING=ON -DUSE_SYSTEM_GTEST=ON"

PACKAGES =+ "${PN}-tests"
FILES:${PN}-staticdev += "${libdir}/libiceoryx2-bb-cxx.a"
RDEPENDS:${PN}-dev += "${PN}-staticdev"
BBCLASSEXTEND = "native nativesdk"

do_install() {
  cmake --install ${S}/../../../build
}

do_install_ptest() {
    install -d ${D}/${PTEST_PATH}/tests
    install -m 0755  ${B}/tests/iceoryx2-bb-cxx-tests ${D}/${PTEST_PATH}/tests
    install -m 0755  ${S}/../../../run-ptest ${D}/${PTEST_PATH}/
}

# NOTE bitbake complains about nobody providing iceoryx2-bb-cxx needed by
#      iceoryx2-bb-cxx-ptest as runtime dependency;
#      since iceoryx2-bb-cxx is a static library
#      we can remove the runtime dependency here:
RDEPENDS:iceoryx2-bb-cxx-ptest:remove = "iceoryx2-bb-cxx"

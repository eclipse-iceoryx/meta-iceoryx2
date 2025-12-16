SUMMARY = "iceoryx2 C examples"
DESCRIPTION = "This package contains the iceoryx2 C examples"
HOMEPAGE = "https://iceoryx.io"
BUGTRACKER = "https://github.com/eclipse-iceoryx/iceoryx2/issues"
LICENSE = "Apache-2.0 | MIT"
LIC_FILES_CHKSUM = "file://LICENSE-APACHE;md5=22a53954e4e0ec258dfce4391e905dac \
                    file://LICENSE-MIT;md5=b377b220f43d747efdec40d69fcaa69d"

inherit cmake

DEPENDS = " \
  iceoryx2-c \
  "

SRC_URI = "git://github.com/eclipse-iceoryx/iceoryx2.git;protocol=https;nobranch=1"

S = "${WORKDIR}/git/examples/c"

RDEPENDS:${PN}-dev += "iceoryx2-c-staticdev"
RDEPENDS:${PN} += "iceoryx2-c"
BBCLASSEXTEND = "native nativesdk"

do_install() {
  install -d ${D}${bindir}/iceoryx2/examples/c

  for example in $(find ${B} -maxdepth 2 -type f -executable); do
    install -m 0755 $example ${D}${bindir}/iceoryx2/examples/c
  done
}

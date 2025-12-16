SUMMARY = "Custom image with all iceoryx2 artifacts"
LICENSE = "Apache-2.0 | MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10 \
                    file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit core-image

# NOTE: iceoryx, iceoryx2 and iceoryx2-bb-cxx are only available as dev/staticdev packages and therefore not listed here
IMAGE_INSTALL:append = "\
  iceoryx2-cli \
  iceoryx2-benchmarks \
  iceoryx2-examples \
  iceoryx2-validation-suite-ptest \
  iceoryx2-c \
  iceoryx2-c-examples \
  iceoryx2-bb-cxx-ptest \
  iceoryx2-cxx \
  iceoryx2-cxx-ptest \
  iceoryx2-cxx-examples \
  "

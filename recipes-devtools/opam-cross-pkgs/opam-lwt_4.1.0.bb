DESCRIPTION = "Promises, concurrency, and parallelized I/O"
SECTION = "devel"
LICENSE = "LGPL"

# IMPORTANT NOTICE:
#
# This package is problematic since it tries to detect system configuration at
# build-time and is not cross-compilation aware.
#
# As a workaround, we provide a 'discover.exe' script that replaces a OCaml tool
# and creates the required config files using pre-defined values. The supplied
# script should work for most ARM installations, other architectures will need
# their own.
# TODO: double-check ARM values

SRC_URI_append_arm = " \
    file://discover.exe_${TARGET_ARCH} \
    file://0001-replace-discover-exe.patch \
    "

inherit opam_install

DEPENDS += " opam-dune opam-cppo opam-result"

do_patch () {
    # dummy function to override built-in do_patch. This way we can use
    # architecture-specific appends below
}

do_patch_append_arm () {
    # copy replacement 'discover.exe'
    install -m 755 ${WORKDIR}/discover.exe_${TARGET_ARCH} \
        ${OPAM_ROOT}/repo/default/packages/lwt/lwt.${PV}/files/discover.exe

    # ignore patch errors that might occur because we may attempt to patch the
    # same file twice as this recipe runs multiple times
    cd ${OPAM_ROOT}/repo/default/packages/lwt/lwt.${PV}
    patch -p1 -f < ${WORKDIR}/0001-replace-discover-exe.patch || true

    # after patching the repo, we need to remove the state cache - it will get
    # rebuild the next time opam runs
    rm ${OPAM_ROOT}/repo/state.cache
}

# more debug output, anyone?
#OPAM_VERBOSE = "1"

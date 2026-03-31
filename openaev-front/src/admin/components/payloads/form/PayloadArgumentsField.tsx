import { DeleteOutlined } from '@mui/icons-material';
import { IconButton } from '@mui/material';
import { useTheme } from '@mui/material/styles';
import { useEffect, useRef } from 'react';
import { Controller, useFormContext } from 'react-hook-form';

import DocumentField from '../../../../components/fields/DocumentField';
import SelectFieldController from '../../../../components/fields/SelectFieldController';
import SeparatorFieldController from '../../../../components/fields/SeparatorFieldController';
import TextFieldController from '../../../../components/fields/TextFieldController';
import { useFormatter } from '../../../../components/i18n';
import type { PayloadArgument } from '../../../../utils/api-types';
import { isFeatureEnabled } from '../../../../utils/utils';
interface Props {
  argumentName: string;
  canSelectTargetAsset: boolean;
  onArgumentRemoveClick: () => void;
}
/** Argument types that have processor sub-fields and therefore support a subtype selection. */
const STRUCTURED_TYPES = new Set<PayloadArgument['type']>(['portscan', 'credentials', 'cve']);
/** Sub-field options keyed by ArgumentType label. */
const SUBTYPE_OPTIONS: Partial<Record<PayloadArgument['type'], {
  value: string;
  label: string;
}[]>> = {
  portscan: [
    {
      value: 'asset_id',
      label: 'Asset ID',
    },
    {
      value: 'host',
      label: 'Host',
    },
    {
      value: 'port',
      label: 'Port',
    },
    {
      value: 'service',
      label: 'Service',
    },
  ],
  credentials: [
    {
      value: 'username',
      label: 'Username',
    },
    {
      value: 'password',
      label: 'Password',
    },
  ],
  cve: [
    {
      value: 'asset_id',
      label: 'Asset ID',
    },
    {
      value: 'id',
      label: 'ID',
    },
    {
      value: 'host',
      label: 'Host',
    },
    {
      value: 'severity',
      label: 'Severity',
    },
  ],
};
const PayloadArgumentsField = ({ argumentName, canSelectTargetAsset, onArgumentRemoveClick }: Props) => {
  const { t } = useFormatter();
  const theme = useTheme();
  const { watch, control, setValue } = useFormContext();
  const argumentType: PayloadArgument['type'] = watch(`${argumentName}.type`);
  /** Types that require the INJECT_CHAINING feature flag to be selectable. */
  const isChainingEnabled = isFeatureEnabled('INJECT_CHAINING');

  /**
   * Track the previous type so the subtype is cleared only when the user
   * actually changes the type — not on initial mount, which would wipe
   * existing subtype values when editing a saved argument.
   */
  const previousTypeRef = useRef<PayloadArgument['type']>(argumentType);
  useEffect(() => {
    if (previousTypeRef.current !== argumentType) {
      setValue(`${argumentName}.subtype`, null);
    }
    previousTypeRef.current = argumentType;
  }, [argumentType, argumentName, setValue]);
  const argumentTypeItems: {
    value: string;
    label: string;
  }[] = [
    // Always available
    {
      value: 'text',
      label: t('Text'),
    },
    {
      value: 'document',
      label: t('Document'),
    },
    ...canSelectTargetAsset
      ? [{
          value: 'targeted-asset',
          label: t('Targeted assets'),
        }]
      : [],
    // Gated behind INJECT_CHAINING feature flag (mirror of ContractOutputType processor types)
    ...(isChainingEnabled
      ? [
          {
            value: 'number',
            label: t('Number'),
          },
          {
            value: 'port',
            label: t('Port'),
          },
          {
            value: 'portscan',
            label: t('Port scan'),
          },
          {
            value: 'ipv4',
            label: t('IPv4'),
          },
          {
            value: 'ipv6',
            label: t('IPv6'),
          },
          {
            value: 'credentials',
            label: t('Credentials'),
          },
          {
            value: 'cve',
            label: t('CVE'),
          },
        ]
      : []),
  ];
  const targetPropertyItems = [
    {
      value: 'hostname',
      label: t('Hostname'),
    },
    {
      value: 'local_ip',
      label: t('Local IP (first)'),
    },
    {
      value: 'seen_ip',
      label: t('Seen IP'),
    },
  ];
  const isStructured = STRUCTURED_TYPES.has(argumentType);
  const subtypeItems = isStructured ? (SUBTYPE_OPTIONS[argumentType] ?? []) : [];
  const columnCount = (() => {
    if (argumentType === 'targeted-asset') return 4;
    if (isStructured) return 4;
    return 3;
  })();
  return (
    <div
      style={{
        display: 'grid',
        gridTemplateColumns: `repeat(${columnCount}, 1fr) auto`,
        gap: theme.spacing(1),
      }}
    >
      <SelectFieldController
        name={`${argumentName}.type` as const}
        label={t('Type')}
        items={argumentTypeItems}
        required
      />
      <TextFieldController name={`${argumentName}.key` as const} label={t('Key')} required />
      {/* Sub-type selector — only for structured output types when chaining is enabled */}
      {isChainingEnabled && isStructured && (
        <SelectFieldController
          name={`${argumentName}.subtype` as const}
          label={t('Sub-type')}
          items={subtypeItems}
        />
      )}
      {(argumentType === 'text' || argumentType === 'number' || argumentType === 'port'
        || argumentType === 'portscan' || argumentType === 'ipv4' || argumentType === 'ipv6'
        || argumentType === 'credentials' || argumentType === 'cve') && (
        <TextFieldController
          name={`${argumentName}.default_value` as const}
          label={t('Default Value')}
          required
        />
      )}
      {argumentType === 'document' && (
        <Controller
          control={control}
          name={`${argumentName}.default_value` as const}
          render={({ field: { onChange, value }, fieldState: { error } }) => (
            <DocumentField
              fieldValue={value ?? []}
              fieldOnChange={onChange}
              label={t('Default Value')}
              error={error}
              style={{ marginTop: 3 }}
            />
          )}
        />
      )}
      {argumentType === 'targeted-asset' && (
        <>
          <SelectFieldController
            name={`${argumentName}.default_value` as const}
            label={t('Targeted property')}
            items={targetPropertyItems}
            required
          />
          <SeparatorFieldController
            name={`${argumentName}.separator` as const}
            label={t('Separator')}
            defaultValue=","
            required
          />
        </>
      )}
      <IconButton
        onClick={onArgumentRemoveClick}
        size="small"
        color="primary"
        data-testid={`${argumentName}.delete-btn`}
      >
        <DeleteOutlined />
      </IconButton>
    </div>
  );
};
export default PayloadArgumentsField;
